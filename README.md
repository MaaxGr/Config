# Configs

Client-Server Application to manage multiple config files.

## Why?

Config-Files (with e.g. Database Password) should not be stored in Git.  
But these config files also are used by different team members, so there should be a way to access and exchange the config files securely

## Solution

This project provides a Client-Server application that can handle multiple configs of multiple users.
The data is stored encrypted in a mysql database.

### Terminology

* A "repository" is a software project that can hold multiple secure config files
* A "config" is a file in a specific repository that is located at a specific path
* A "variant" is a concrete config file. If everyone should use the same config, there is only one variant per config. But there can also be multiple variants per config. (e.g. one Variant per User or Device)

## Getting Started

Comming Soon

## Roadmap

* [ ] Finish Backend Service in Kotlin/Ktor
* [ ] Write CLI Program in Rust

## Credits

This project is inspired by [filetailor](https://github.com/k4j8/filetailor).
