(ns clj-totp.core
  (:import [com.warrenstrange.googleauth
            GoogleAuthenticator
            GoogleAuthenticatorKey
            GoogleAuthenticatorQRGenerator]))

(set! *warn-on-reflection* true)

(defn- key->qr-code-url [^GoogleAuthenticatorKey key issuer account-name]
  (GoogleAuthenticatorQRGenerator/getOtpAuthURL issuer account-name key))

(defn generate-key
  "Returns a newly generated key"
  [issuer account-name]
  (let [key (.createCredentials (new GoogleAuthenticator))]
    {:secret-key (.getKey key)
     :qr-code-url (key->qr-code-url key issuer account-name)
     :scratch-codes (into [] (.getScratchCodes key))}))

(defn valid-code?
  "Validates a code"
  ([^String secret-key ^long code]
   (.authorize (new GoogleAuthenticator) secret-key code))
  ([^String secret-key ^long code ^long time]
   (.authorize (new GoogleAuthenticator) secret-key code time)))

(defn generate-otp
  "Generates a TOTP Password. For Testing Only"
  ([^String secret-key]
   (.getTotpPassword (new GoogleAuthenticator) secret-key))
  ([^String secret-key ^long time]
   (.getTotpPassword (new GoogleAuthenticator) secret-key time)))
