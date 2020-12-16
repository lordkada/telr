#!/usr/bin/env sh

if [ ! -d "./devops" ]; then
  echo "This script must be run from the base project folder";
  exit 1
fi;

if [ -z "$VERSION" ]; then
    echo "VERSION variable not defined";
    exit 1
fi;

FROM_DIR="$(pwd)"

export BUILD_FOLDER=$FROM_DIR"/build_local"
echo "$BUILD_FOLDER"
mkdir -p $BUILD_FOLDER
echo "Building in temporary folder:" $BUILD_FOLDER

cp ./devops/Dockerfile $BUILD_FOLDER
cp -fr ./src $BUILD_FOLDER
cp ./pom.xml $BUILD_FOLDER

cd "$BUILD_FOLDER" || exit

docker run --user "$(id -u):$(id -g)" -it --rm --name my-maven-project -v "$BUILD_FOLDER":/usr/src/mymaven -w /usr/src/mymaven maven:3.6-jdk-8 mvn clean package

docker build -t lordkada/telr:$VERSION .

cd $FROM_DIR || exit

echo "Cleaning up temporary folder"
rm -fr $BUILD_FOLDER