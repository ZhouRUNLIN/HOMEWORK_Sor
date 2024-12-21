# Crud-books

A simple REST server build with JS on a pg DB.

### Install dependencies

```sh
yarn # or npm i
```

### Run pg migrations

```sh
yarn migrate # or npm run migrate
```

### Run server

```sh
yarn start # or npm run start
```

When server is up :

##### root

```
localhost:PORT => 'OK'
```

##### request per id

```
localhost:PORT/books?id=2fb4d4e0-4625-49b3-add0-b68e13072254 =>
```

```json
[
  {
    "id": "2fb4d4e0-4625-49b3-add0-b68e13072254",
    "info": {},
    "created_at": "2020-12-12T18:42:26.182Z",
    "updated_at": "2020-12-12T18:42:26.182Z"
  }
]
```

##### create/update

```
localhost:PORT/books/create?info={}
```

=> '2fb4d4e0-4625-49b3-add0-b68e13072254'

```
localhost:PORT/books/create?info={}&id=2fb4d4e0-4625-49b3-add0-b68e13072254
```

=> '2fb4d4e0-4625-49b3-add0-b68e13072254'
