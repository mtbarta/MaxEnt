(ns maxent.core-test
  (:require [clojure.test :refer :all]
            [maxent.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest tokenize-test
  (test "tokenize"
       (is (= (["this" "is" "a" "test"]) (tokenize ["this is a test"])))
       (is (= (["thi" "i" "a" "te" "t"]) (tokenize "s" ["this is a test"])))))

(deftest vector-to-map-test
  (is (= {two no, one yes} (vector-to-map ["one" "two"] ["yes" "no"]))))

(run-tests)
