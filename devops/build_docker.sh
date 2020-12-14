#!/usr/bin/env sh

if [ ! -d "./devops" ]; then
  echo "This script must be run from the base project folder";
  exit 1
fi;

if [ -z "$VERSION" ]; then
    echo "\$VERSION variable not defined";
    exit 1
fi;

from_dir="$(pwd)"

export BUILD_FOLDER="$(mktemp -d)"
echo "Building in temporary folder:" $BUILD_FOLDER

cp "$from_dir/devops/Dockerfile" "$BUILD_FOLDER"

cd "$BUILD_FOLDER" || exit

git clone -b $VERSION --single-branch git@github.com:lordkada/telr.git repo

BUILD_FOLDER_REPO=$BUILD_FOLDER"/repo"

docker run --user "$(id -u):$(id -g)" -it --rm --name my-maven-project -v "$BUILD_FOLDER_REPO":/usr/src/mymaven -w /usr/src/mymaven maven:3.6-jdk-8 mvn clean package

docker build -t lordkada/telr:$VERSION .

cd $from_dir || exit

echo "Cleaning up temporary folder"
rm -fr $BUILD_FOLDER