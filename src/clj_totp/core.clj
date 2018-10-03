(ns clj-totp.core
  (:import [com.warrenstrange.googleauth
            GoogleAuthenticator
            GoogleAuthenticatorConfig$GoogleAuthenticatorConfigBuilder
            GoogleAuthenticatorKey
            GoogleAuthenticatorQRGenerator]))

(set! *warn-on-reflection* true)

(def ^{:dynamic true :private true}
  ^GoogleAuthenticator authenticator
  (new GoogleAuthenticator))

(defn- key->qr-code-url [^GoogleAuthenticatorKey key issuer account-name]
  (GoogleAuthenticatorQRGenerator/getOtpAuthURL issuer account-name key))

(defn generate-key
  "Returns a newly generated key"
  [issuer account-name]
  (let [key (.createCredentials authenticator)]
    {:secret-key (.getKey key)
     :qr-code-url (key->qr-code-url key issuer account-name)
     :scratch-codes (into [] (.getScratchCodes key))}))

(defn valid-code?
  "Validates a code"
  ([^String secret-key ^long code]
   (.authorize authenticator secret-key code))
  ([^String secret-key ^long code ^long time]
   (.authorize authenticator secret-key code time)))

(defn generate-otp
  "Generates a TOTP Password. For sending via email/sms"
  ([^String secret-key]
   (.getTotpPassword authenticator secret-key))
  ([^String secret-key ^long time]
   (.getTotpPassword authenticator secret-key time)))

(defmacro with-authenticator [the-authenticator & body]
  `(binding [authenticator ~the-authenticator]
     ~@body))

(defn ^GoogleAuthenticator build-authenticator [& {:keys [time-step window-size code-digits]}]
  (let [builder (cond-> (new GoogleAuthenticatorConfig$GoogleAuthenticatorConfigBuilder)
                  time-step (.setTimeStepSizeInMillis time-step)
                  window-size (.setWindowSize window-size)
                  code-digits (.setCodeDigits code-digits))]
    (new GoogleAuthenticator (.build builder))))
