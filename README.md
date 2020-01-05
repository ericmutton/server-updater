# server-updater
A Minecraft server updater made with Java. Reads `server.jar` or `logs/latest.log` file to find server version then checks for newer versions. Can optionally check, download, and then update the vanilla Minecraft server Jar executable.

## Getting Started

### Prerequisites

* [Minecraft server](https://www.minecraft.net/en-us/download/server)

## How to Run

### CLI Usage
#### OPTIONS
`includes` `--snapshot` or `--release` or `--version="id"`. `<includes>` defaults to `--release`.
`auto`:`--auto-update`
#### ARGUMENTS
`dir`: directory of parent folder of server jar.
#### SYNOPSIS
```bash
java -jar server-updater.jar [options] dir
```
## How to Use
* Designed to be run before the server jar in the start script.
* Questions may be answered using `yY` or `nN`

## Libraries

* [json-simple-1.1.1](https://code.google.com/archive/p/json-simple/downloads) - Used for JSON support
* [commons-io-2.6](http://commons.apache.org/proper/commons-io/download_io.cgi) - Used to download files

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
