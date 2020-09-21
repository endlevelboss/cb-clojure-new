(defproject cb-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.3.610"]
                 [org.clojure/clojurescript "1.10.773"]
                 [reagent "1.0.0-alpha2"]
                 [testdouble/clojurescript.csv "0.4.5"]
                 [tonysort "0.1.0-SNAPSHOT"]]
  :plugins [[lein-cljsbuild "1.1.8"]]
  :cljsbuild {
              :builds [{:source-paths ["src"]
                        :compiler {:output-to "resources/public/core.js"}}] 
              }
  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.2.11"]
                                  [binaryage/devtools "1.0.2"]
                                        ;[com.bhauman/rebel-readline-cljs "0.1.4"]
                                  ]
                   :resource-paths ["target"]
                   :clean-targets ^{:protect false} ["target"]}}

  :repl-options {:init-ns cb-test.core})
