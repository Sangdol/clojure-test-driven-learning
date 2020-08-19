# Clojure tests

## Clojure tests

### How to run

[test-refresh](https://github.com/jakemcc/lein-test-refresh)

```sh
lein test-refresh
lein test-refresh :changes-only
```

Or,

```sh
./test-cl.sh
```

## ClojureScript tests

### How to run

[doo](https://github.com/bensu/doo)

```sh
lein doo

lein doo {js-env}

lein doo {js-env} {build-id}

lein doo {js-env} {build-id} {watch-mode}
```

For example,
```sh
lein doo chrome-headless test
``` 

Or,

```sh
./test-js.sh
```

### Installation to run tests with chrome-headless

```sh
npm install karma --save-dev && \
    npm install karma-cljs-test --save-dev && \
    npm install karma-chrome-launcher --save-dev
```

## Etc.

### Class Diagram

https://github.com/stuartsierra/class-diagram

```clojure
(require '[com.stuartsierra.class-diagram :as diagram])
(diagram/view clojure.lang.PersistentList)
```