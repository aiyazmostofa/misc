# https://github.com/andir/npins/blob/master/shell.nix
let
  pins = import ./npins;
  pkgs = import pins.nixpkgs { };
in
pkgs.mkShell {
  nativeBuildInputs = with pkgs; [
    clang-tools
    emscripten
    bear
    oxfmt
  ];
}
