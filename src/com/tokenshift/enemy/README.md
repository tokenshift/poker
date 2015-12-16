# Enemy

A simple authentication framework for [Ring](https://github.com/ring-clojure/ring)
apps, inspired by [Omniauth](https://github.com/intridea/omniauth) (and not in
any way by [Friend](https://github.com/cemerick/friend)).

## Use

Enemy provides Ring middleware to handle user authentication using providers
(strategies) like Amazon, Facebook, or Google. Client application configuration
is required by most strategies; you will need to register your client app with
the relevant service as usual, Enemy doesn't handle this for you.

Enemy doesn't force anyone to log in to access your service, or provide any
additional authorization mechanisms for your app to use; it simply adds any
user information provided by the strategy in use to the Ring request for you to
do whatever you want with.

Hook up Enemy by adding it (and any strategies you want to use) to your app's
middleware stack:

```clojure
(require '[com.tokenshift.enemy :refer [wrap-strategies basic-strategy]]
         '[com.tokenshift.enemy.google :refer [google-strategy]])

(-> app
    (wrap-strategies "auth"
      "basic"  (basic-auth-strategy "username" "password")
      "google" (google-strategy "google-oauth2-client-app-id"
                                "google-oauth2-client-secret")))
```

To request authentication, simply redirect the user to the authorization URL for
the desired strategy (e.g. `/auth/google`). Usually you would do this from a
custom login page in your app listing the authorization options.

Once authentication completes, your app will receive a request to e.g.
`/auth/google/callback`, with any available authentication/user information
added under the :auth key.

Most strategies will add user details to the session, so you will probably want
a session middleware hooked up to persist this information across page loads.