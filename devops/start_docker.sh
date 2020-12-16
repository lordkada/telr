#!/usr/bin/env sh
if [ -z "$VERSION" ]; then
    echo "VERSION variable not defined";
    exit 1
fi;

docker run --rm -it -p8080:8080 lordkada/telr:$VERSION