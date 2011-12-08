(ns clj-ua.test.core
  (:use [clj-ua.core])
  (:use [clojure.test]))

(def agents ["Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Mobile/8A293"
             "Opera/9.80 (Macintosh; Intel Mac OS X; U; en) Presto/2.6.30 Version/10.62"
             "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10"
             "Feedfetcher-Google; (+http://www.google.com/feedfetcher.html; 4 subscribers; feed-id=9048986372668769311)"
             "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13"
             "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.14) Gecko/20110218 Firefox/3.6.14"
             "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.14) Gecko/20110218 Firefox/3.6.14"
             "Opera/9.80 (Windows NT 5.1; U; ru) Presto/2.6.30 Version/10.62"
             "Opera/9.80 (Macintosh; Intel Mac OS X; U; en) Presto/2.6.30 Version/10.62"
             "Mozilla/5.0 (Linux; U; Android 2.2; en-us; ADR6300 Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1"
             "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en-US; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8"
             "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_3; fr-fr) AppleWebKit/533.16 (KHTML, like Gecko) Version/5.0 Safari/533.16"
             "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; en-us) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8"
             "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; es-es) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8"
             "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; fr-fr) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8"
             "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8"
             "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en-US; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8"
             "Mozilla/5.0 (iPod; U; CPU iPhone OS 3_1_3 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7E18 Safari/528.16"
             "Mozilla/5.0 (iPod; U; CPU iPhone OS 4_0_2 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A400 Safari/6531.22.7"
             "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B405 Safari/531.21.10"
             "Mozilla/5.0 (iPad; U; CPU OS 3_2_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B500 Safari/531.21.10"
             "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_1_3 like Mac OS X; it-it) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7E18 Safari/528.16"
             "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0_1 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Mobile/8A306"
             "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0_2 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A400 Safari/6531.22.7"
             "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Mobile/8A293"
             "Mozilla/5.0 (iPod; U; CPU iPhone OS 3_1_2 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7D11 Safari/528.16"
             "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; en-us) AppleWebKit/533.16 (KHTML, like Gecko) Version/5.0 Safari/533.16"
             "Mozilla/5.0 (iPod; U; CPU iPhone OS 3_1_3 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Mobile/7E18"
             "Mozilla/5.0 (iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10"
             "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10"
             "Mozilla/5.0 (X11; U; Linux i686; pt-BR; rv:1.9.1.8) Gecko/20100214 Ubuntu/9.10 (karmic) Firefox/3.5.8 MEGAUPLOAD 1.0 GTB7.1"
             "Mozilla/5.0 (compatible; DotBot/1.1; http://www.dotnetdotcom.org/, crawler@dotnetdotcom.org)"
             "Mozilla/5.0 (Twiceler-0.9 http://www.cuil.com/twiceler/robot.html)"])

(deftest test-parse
  (loop [i 0]
    (when (< i (count agents))
      (let [agent (nth agents i)]
        (parse agent))
      (recur (inc i)))))
