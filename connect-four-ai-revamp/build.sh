mkdir -p out
bear -- emcc -o out/engine.mjs engine.cpp -s "NO_EXIT_RUNTIME=1" -s "EXPORTED_RUNTIME_METHODS=['ccall']"
