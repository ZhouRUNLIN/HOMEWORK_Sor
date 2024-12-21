# Books database

This repo is the base for build a postgres docker image.
It create a user books for the database books. (You will need it to connect with a server)

```sh
docker build -t database .
```

```sh
docker run -p 5432:5432 -d -t --restart unless-stopped database
```
