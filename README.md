# server-updater
A Minecraft server updater made with Java. Reads a server's `latest.log` file to find server version then checks for newer versions. Can optionally check, download, and then update the vanilla Minecraft server Jar executable.

## Getting Started

### Prerequisites

* [Java 8](https://www.java.com/en/download/manual.jsp)

* [Minecraft server](https://www.minecraft.net/en-us/download/server)

## How to Run

`includes`: `--snapshot` or `--release`

`dir`: directory of parent folder of server jar

```batch
java -jar server-updater.jar <includes> <dir>
```
## How to Use
* Designed to be run before the server jar in the start script, CLI only.
* Questions may be answered using `yY` or `nN`

## Libraries

* [json-simple-1.1.1](https://code.google.com/archive/p/json-simple/downloads) - Used for JSON support
* [commons-io-2.6](http://commons.apache.org/proper/commons-io/download_io.cgi) - Used to download files

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
