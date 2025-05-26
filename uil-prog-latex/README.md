# UIL Programming Latex Template

Here is my latex template for a UIL programming packet.

## Setup & Requirements

Along with the templates, this comes with an entire build process,
which has many different dependencies. This is easy for me, because I
already have all of these packages. But this might be a challenge to
setup for most.

| Dependency   | Arch Package Name |
| ------------ | ----------------- |
| Latex        | `texlive-full`    |
| Pandoc       | `pandoc`          |
| Go           | `go`              |
| Node.js      | `nodejs`          |
| NPM          | `npm`             |
| clang-format | `clang`           |
| bash         | `bash`            |

Then run:

```sh
bash ./scripts/install.sh
```

## Build

Run:

```sh
bash ./scripts/build.sh
```

## Working

Look at the provided problem called sum.

If you are ready to commit, you can run the `./scripts/format.sh`
script.