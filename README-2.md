# TODO
- get CI working
- get incremental builds working

- we have a build system issue where the currently vendored cucumber requires mockito 3.6.28 which isnt accessible anymore on whichever artifactory? we archive it and copy it into the build process as a temporary workaround.

#misc tools:
- `find_layer() { cat /var/lib/containers/storage/overlay-layers/layers.json | jq '.[] | select((.. | strings) | contains("'"$1"'"))'; }`
- `find_layer() { cat /var/lib/containers/storage/overlay-images/images.json | jq -r '.[] | select((.. | strings) | contains("'"$1"'")) | .layer' | uniq; }`
- `find /var -iname "*$(find_layer 14c977e8fdcf)*"`