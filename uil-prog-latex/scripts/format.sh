#!/bin/bash
cd scripts
ls
npx prettier ../ --write
clang-format -i ../*/Solution.java
