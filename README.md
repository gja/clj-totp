# clj-totp - Google Authenticator

An unopinionated library for generating and validating TOTP with Google Authenticator.

## Usage

Add the following to your dependencies:

```clojure
[clj-totp "0.1.0"]
```

And import the following namespace
```
(:import [clj-totp.core :as totp])
```

```clojure
(totp/generate-key "Tejas Dinkar" "tejas@example.com") ; => {:secret-key "J5H6AG3ZANCKLTBK", :qr-code-url "https://chart.googleapis.com/chart?chs=200x200&chld=M%7C0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2FTejas%2520Dinkar%3Atejas%40example.com%3Fsecret%3DJ5H6AG3ZANCKLTBK%26issuer%3DTejas%2BDinkar", :scratch-codes [89711635 26333477 46096035 19284027 63627698]}

(comment "somehow persist the secret-key, and scan the qr-code-url with google authenticator")

(totp/valid-code? "J5H6AG3ZANCKLTBK" 850602 1482538483070) ; => true (params: secret-key, password, time (optional))
(totp/valid-code? "J5H6AG3ZANCKLTBK" 000000) ; => false
```

## License

Copyright © 2016 Tejas Dinkar

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
