(ns clj-totp.core-test
  (:require [clojure.test :refer :all]
            [clj-totp.core :refer :all]))

(deftest it-validates-a-password
  (let [{:keys [secret-key]} (generate-key "Testing" "test@example.com")
        code (generate-otp secret-key)]
    (is (valid-code? secret-key code))

    (is (not (valid-code? secret-key (inc code))))

    (is (not (valid-code? secret-key code (- (System/currentTimeMillis) 90000))))))

(deftest it-can-take-different-configs
  (with-authenticator (build-authenticator :code-digits 8)
    (let [{:keys [secret-key]} (generate-key "Testing" "test@example.com")
          code (generate-otp secret-key)]
      (is (valid-code? secret-key code))
      (is (<= 1000000 code 99999999)))))
