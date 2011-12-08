(ns clj-ua.core
  (:require [clojure.contrib.command-line :as ccl]
            [clojure.string :as string]))

(defrecord Browser [name version])
(defrecord Platform [name version])
(defrecord UserAgent [#^Browser browser
                      #^Platform platform])

(defn log [data]
  (prn (merge {:ns "clj-ua"} data)))

(def parsed-browser (ref {}))
(def parsed-platform (ref {}))

(defn parse-browser [#^String seq]
  (loop [i 0]
    (when (< i (count seq))
      (let [part (nth seq i)]
        (dosync
          (if (not (.equals "" (nth part 0)))
            (alter parsed-browser assoc :name (nth part 1) :version (nth part 3)))))
      (recur (inc i)))))

(defn set-os [#^String part]
  (dosync
    (let [version (re-find #"([0-9]+)[_.]([A-Za-z0-9]+)" part)]
      (if (nil? version)
        ; then
        (let [second-guess (re-find #"OS X" (.toUpperCase part))]
          (if (not (nil? second-guess))
            (alter parsed-platform assoc :name part :version second-guess)))
        ; else
        (alter parsed-platform assoc :name part :version (.replaceAll (nth version 0) "_" "."))))))

(defn handle-comment [parts callback]
  (if (< 1 (count parts))
    (let [candidate (nth parts 2)]
      (callback candidate))))

(defn parse-comment [#^String seq #^String comment callback]
  (let [is-mozilla (.startsWith (nth (nth seq 0) 0) "Mozilla")
        parts (.split comment "; ")]
    (if (and is-mozilla (.equals "compatible" (nth parts 0)))
      ; then
      (let [real-browser (nth parts 1)]
        (if (< 0 (.indexOf "/" real-browser))
          ; then we have product/version
          (dosync
            (alter parsed-browser assoc :name (.substring real-browser 0 (.indexOf real-browser "/")) :version (.substring real-browser (+ (.indexOf real-browser "/") 1))))
          ; else we'll have to decipher text
          (dosync
            (alter parsed-browser assoc :name (.substring real-browser 0 (.indexOf real-browser " ")) :version (.substring real-browser (+ (.indexOf real-browser " ") 1))))))
      ; else
      (handle-comment parts callback))))

(defn do-parse [#^String seq]
  (parse-browser seq) ; first cut at getting browser
  (loop [i 0]
    (when (< i (count seq))
      (let [part (nth seq i)]
        (if (< 1 (count part))
          ; then
          (let [comment (nth part 6)]
            (if (and (not (nil? comment)) (nil? (:name @parsed-platform)))
              (parse-comment seq comment (fn [candidate] (set-os candidate)))))
          ; else
          (fn []
            (if (and (not (nil? part)) (nil? (:name @parsed-platform)))
              (parse-comment seq part (fn [candidate] (set-os candidate)))))))
      (recur (inc i)))))

(defn parse [#^String agent]
  (log {:fn "parse" :agent agent})
  (dosync
    (alter parsed-browser dissoc :name :version)
    (alter parsed-platform dissoc :name :version))
  (let [seq (re-seq #"([^/\s]*)(/([^\s]*))?(\s*\[[a-zA-Z][a-zA-Z]\])?\s*(\((([^()]|(\([^()]*\)))*)\))?\s*" agent)]
    (do-parse seq)
    (let [browser (Browser. (:name @parsed-browser) (:version @parsed-browser))
          platform (Platform. (:name @parsed-platform) (:version @parsed-platform))
          user-agent (UserAgent. browser platform)]
      (log {:user-agent user-agent}))))

(defn -main [& args]
  (if args
    (let [agent (nth args 0)]
      (parse agent))
    (prn "Please pass a user-agent to parse.")))
