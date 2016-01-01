;;; https://github.com/weavejester/crypto-password
(ns clojure-learning-test.crypto-password-test
  (:require [crypto.password.bcrypt :as bcrypt]
            [clojure.test :refer :all]))

(deftest bcrypt-test
  "
  * Use bcrypt to save password safely
    * http://stackoverflow.com/questions/4494234/what-are-the-best-practices-to-encrypt-passwords-stored-in-mysql-using-php
    * http://codahale.com/how-to-safely-store-a-password/
  * 50 bytes is the good max password lenth
    * http://security.stackexchange.com/questions/39849/does-bcrypt-have-a-maximum-password-length
  * Brypt have built-in salt
    * it generates randomly and take it later
    * http://stackoverflow.com/questions/6832445/how-can-bcrypt-have-built-in-salts
  "
  (def encrypted (bcrypt/encrypt "1"))
  (def encrypted2 (bcrypt/encrypt "1"))

  (is (bcrypt/check "1" encrypted))
  (is (bcrypt/check "1" encrypted2))

  (is (not= encrypted encrypted2))

  ;; length of a bcrypt hashed password?
  ;; http://stackoverflow.com/questions/5881169/what-column-type-length-should-i-use-for-storing-a-bcrypt-hashed-password-in-a-d
  (is (>= 60 (count encrypted)))
  )
