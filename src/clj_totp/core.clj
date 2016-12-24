(ns clj-totp.core
  (:import [com.warrenstrange.googleauth
            GoogleAuthenticator
            GoogleAuthenticatorKey
            GoogleAuthenticatorQRGenerator]))

(set! *warn-on-reflection* true)

(defn- key->qr-code-url [^GoogleAuthenticatorKey key issuer account-name]
  (GoogleAuthenticatorQRGenerator/getOtpAuthURL issuer account-name key))

(defn generate-key [issuer account-name]
  (let [key (.createCredentials (new GoogleAuthenticator))]
    {:secret-key (.getKey key)
     :qr-code-url (key->qr-code-url key issuer account-name)
     :scratch-codes (into [] (.getScratchCodes key))}))

(defn validate-password
  ([^String secret-key ^long password]
   (.authorize (new GoogleAuthenticator) secret-key password))
  ([^String secret-key ^long password ^long time]
   (.authorize (new GoogleAuthenticator) secret-key password time)))
