(ns symautre.voidnet.posts
  (:require-macros [hiccups.core :as hiccups :refer [html]])

  (:require [reagent.core :as r]
            [reagent.dom :as rd]
            [react]
            [react-dom]
            [clojure.core.async :as a]
            cljs.reader

            cljs.js
            [symautre.tools.core :as t :refer [$ -->]]
            ;; [ipfs-core]


            [taipei-404.html :refer [html->hiccup]]
            [force-graph :as fg :refer [ForceGraph]]
            [cljs-http.client :as http]
            ;; [react-force-graph]
            ;; [react-force-graph-2d :refer [ForceGraph2D]]
            [symautre.ui-body :refer [body]]
            symautre.local-storage
            ajax.core

            [symautre.doc :as doc]

            [hiccups.runtime :as h]
            [thi.ng.color.core :as col]))

(defn token
  ([]
   {
    :id (t/uuid)
    :timestamp (t/timestamp-unix)
    :body #{}
    })

  ([m]
   (merge (token) m)))


(defn img
  [url]
  (fn [url]
    [:img {:style {:max-width "50%"}, :src url}]))

(def data
  (transduce (comp (map #(if (contains? % :timestamp.unix)
                           (dissoc (assoc % :timestamp (:timestamp.unix %)) :timestamp.unix)
                           %))

                   (map #(if-not (contains? % :timestamp)
                           (assoc % :timestamp 0)
                           %)))
             conj
             [
              {:id "512c7eef-0b69-4959-985d-ecc94be1315a",
               :type :document,
               :timestamp.unix 1703316358248,
               :timestamp "2023-12-23T07:25:58.248Z", 
               :ipfs/id ""
               :title "crime trash"
               :body [
                      "war trash! 
one million days of crying on heaps of garbage
"
                      ]
               :author nil
               :under "that design"
               :at "that design"
               }

              {
               :id "aed21d96-ddea-4352-ba33-a5f89298dcb7", :type :document, :timestamp.unix 1703390743821, :timestamp "2023-12-24T04:05:43.821Z",
               , :ipfs/id ""
               :code (pr-str '(let [xs #{"grapes of wrath"
			                 "death star"
			                 "parallax recursion"}]
		                (into [:div]
                                      (for [x xs]
			                [:p x]
			                ))))
               :title "Canopy GrapeUnit -> Vacant Orb"
               :body [:div

                      [:p "grapes of wrath"]
                      [:p "death star"]
                      [:p "parallax recursion"]]
               }

              {
               :id "c7409452-e7f0-4075-844c-75dc0ed8fb08",
               :timestamp.unix 1703586650393,
               :timestamp "2023-12-26T10:30:50.393889795Z",
               :body "(fn [everything & more])"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               }


              {:id "da4f8968-99cc-4106-ac99-89a25ac77239",
               :timestamp.unix 1703668060612,
               :timestamp "2023-12-27T09:07:40.612473625Z",
               :body [:div.w3-container
                      [:img.w3-container {:style {:width "50%" :border-radius "10%"}
			                  :src "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/caebfb7c-b1d6-4625-99f8-568f1aba2044/dgmp36b-c0c43ea1-10c2-468b-859e-89fb8aa22279.jpg/v1/fit/w_800,h_600,q_70,strp/webcam_toy_photo15_by_likebad_dgmp36b-414w-2x.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NjAwIiwicGF0aCI6IlwvZlwvY2FlYmZiN2MtYjFkNi00NjI1LTk5ZjgtNTY4ZjFhYmEyMDQ0XC9kZ21wMzZiLWMwYzQzZWExLTEwYzItNDY4Yi04NTllLTg5ZmI4YWEyMjI3OS5qcGciLCJ3aWR0aCI6Ijw9ODAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.uF2levxQhm0sgPCr-SV5Wz4_sliA-2SwuGoXDHKrZ0k"

			                  ;; "https://ipfs.io/ipfs/QmTXfDvieSSHno7HkwTrWcPFGDDMMZQxdwYxKdsAqNuSHF?filename=webcam_toy_photo15_by_likebad_dgmp36b-414w-2x.jpg"
			                  }]

                      [:img.w3-container {:style {:width "50%" :border-radius "10%" :transform "scaleY(-1) scaleX(-1)"}
			                  :src "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/caebfb7c-b1d6-4625-99f8-568f1aba2044/dgmp3rp-63287694-7960-4daa-9741-b321374aebb7.jpg/v1/fit/w_800,h_600,q_70,strp/webcam_toy_photo9_by_likebad_dgmp3rp-414w-2x.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NjAwIiwicGF0aCI6IlwvZlwvY2FlYmZiN2MtYjFkNi00NjI1LTk5ZjgtNTY4ZjFhYmEyMDQ0XC9kZ21wM3JwLTYzMjg3Njk0LTc5NjAtNGRhYS05NzQxLWIzMjEzNzRhZWJiNy5qcGciLCJ3aWR0aCI6Ijw9ODAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.lBfhTg_-BG-Ujc0grlrddhjBMUQz7wDOiIwkhxRQu98"
			                  ;; "https://ipfs.io/ipfs/QmRqjAAUYUDND82bPpGg2RQJWa5MpaNxwpZRbuZyvJtDaQ?filename=webcam_toy_photo9_by_likebad_dgmp3rp-414w-2x.jpg"
			                  }]
                      
                      ],
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {
               :id "4b674fdd-bd4b-4335-8111-2edd927690ee",
               :timestamp.unix 1703748212964,
               :timestamp "2023-12-28T07:23:32.964945586Z",

               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               :body [:p "portal candy-crush angryBirds"]
               }

              {:id "a525ab49-cd1b-4e6e-878a-edd009416043",
               :timestamp.unix 1703748448942,
               :timestamp "2023-12-28T07:27:28.942551501Z",
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               :body ["odd in hour"
                      "slaughterhouse #5"
                      "loom video game, wizard of oz"
                      "capitalDissimulation"
                      "compπuter"]}
              
              {
               :id "d9eac6e4-0f54-4196-a7f2-51e8a2384058",
               :timestamp.unix 1703827108507,
               :timestamp "2023-12-29T05:18:28.507375297Z",
               :body
               [:div.w3-container
                [:h1 "triVium SuiCide"]
                [:img.w3-container {:style {:width "50%" :border-radius "10%"}
                                    :src "https://c4.wallpaperflare.com/wallpaper/477/516/897/bubbles-purple-wallpaper-preview.jpg"}]
                [:br]
                [:br]
                [:br]
                
                ]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}
              

              {:id "c31ce9be-8be0-4117-9943-42780501e7b5",
               :timestamp.unix 1703980427704,
               :timestamp "2023-12-30T23:53:47.705323554Z",
               :body [:p "sacrophagus, sacrificial pit, sacred"]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              #_{:id "9d9924a0-df27-4c1b-94af-68f82085dfae",
                 :timestamp.unix 1703980459484,
                 :timestamp "2023-12-30T23:54:19.484348204Z",
                 :body [:p "zugangenheit ~ attitude"]
                 :actor/public-key-ed25519
                 "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
                 
                 }
              
              {:id "87b6757c-486d-4cc5-8346-ff75a26e57cc",
               :timestamp.unix 1704467302529,
               :timestamp "2024-01-05T15:08:22.529410947Z",
               :body [:div [:p "#{#{\"ram\" \"obsidian\" \"nite\"} }"]
	              [:p "plateau 1"]
	              
	              ],
               :plateau 1
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "8031cfb4-4165-43ef-b8a7-7fe00ee922bb",
               :timestamp.unix 1704534857782,
               :timestamp "2024-01-06T09:54:17.782236945Z",
               :body [:p "viewer techtonics"],
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               }

              {:id "c8a3386d-dd6f-4812-b815-d906dc73f379",
               :timestamp.unix 1704648795193,
               :timestamp "2024-01-07T17:33:15.193509246Z",
               :body #{"march into the sea" "walk into the bar"},
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"

               }

              {:id "f9dff282-6f79-4c21-9762-4cb449eda207",
               :timestamp.unix 1704646773782,
               :timestamp "2024-01-07T16:59:33.782652331Z",
               :body ["vacant falls"
                      "cloudConnected"
                      ]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               
               }

              {:id "67f8fdf6-f48b-4dad-98f9-3646f061c1df",
               :timestamp.unix 1704649230092,
               :timestamp "2024-01-07T17:40:30.092458310Z",
               :body "red blue tree",
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               }

              {:id "e08fe457-38df-4ac4-b772-a525fdafcf15",
               :timestamp.unix 1704713617879,
               :timestamp "2024-01-08T11:33:37.880121965Z",
               :body "neon sine"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"

               }

              {:id "5fcbf474-b681-408d-9465-bde05616ea22",
               :timestamp.unix 1705482014584,
               :timestamp "2024-01-17T09:00:14.584750653Z",
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               :body "hypercube chance dice in fist, in the glass"

               }

              {:id "a0e165b7-cfaf-4a33-b246-4ced1ac8e413",
               :timestamp.unix 1705482042797,
               :timestamp "2024-01-17T09:00:42.797955992Z",

               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               :body "aaah aaah monad. that's interesting."
               }

              {:id "d34f680f-e6f8-45a8-8f79-250e0e38d0da",
               :timestamp.unix 1705496369754,
               :timestamp "2024-01-17T12:59:29.754206532Z",
               :body "circuit maul"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               }

              {:id "76818756-28c5-47c4-bc33-ccf4eeb2b1e5",
               :timestamp.unix 1705497836338,
               :timestamp "2024-01-17T13:23:56.338577522Z",
               :body "chain bridges"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"

               }

              {:id "e9db8d0d-0322-40a5-be43-fcd968bc3130",
               :timestamp.unix 1705498376587,
               :timestamp "2024-01-17T13:32:56.587851321Z",
               :body "vacant angel tyrael"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "0b1cf786-eb8d-436e-83ab-2c6c7673bff6",
               :timestamp.unix 1705499869316,
               :timestamp "2024-01-17T13:57:49.316392698Z",
               :body "hyperdice"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {
               :id "d3557d74-444f-4fcd-900a-f56aa2a2f63d",
               :timestamp.unix 1705551540735,
               :timestamp "2024-01-18T04:19:00.736199110Z",
               :body [:div
                      [:p "other side conveyor belt of data"]
                      [:p "scorpion falls"]
                      [:p "6corpi9n hole-o-gram"]
                      [:p "hole-o-gram manifold"]
                      ]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "6b9176b0-0715-4d39-b4d6-638de612735d",
               :timestamp.unix 1705551763352,
               :timestamp "2024-01-18T04:22:43.352455012Z",
               :body ["or bit" "qua mechanics" "celest mechanics"]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {
               :id "7cf58ac4-c42c-4194-a888-44f77ad22ac5",
               :timestamp.unix 1705552274588,
               :timestamp "2024-01-18T04:31:14.588919247Z",
               :body {
                      :equivalence #{}
                      :bondage/synoname #{:wedding
                                          "spider dense-city"
                                          
                                          }
                      :bondage/instance #{}}
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "a5f661cb-3e0f-4c79-aabc-a82190367600",
               :timestamp.unix 1705552402776,
               :timestamp "2024-01-18T04:33:22.776740263Z",
               :body "capital O used to be empty place"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "7374e4cc-a386-48d5-aa54-83f196ffbe0f",
               :timestamp.unix 1705552500317,
               :timestamp "2024-01-18T04:35:00.317490261Z",
               :body ["one-counters bank" "one-makers bank"]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "8a7439fd-5606-4baa-ae61-09ada5c34c68",
               :timestamp.unix 1705552803673,
               :timestamp "2024-01-18T04:40:03.673652554Z",
               :body ["delay of reaction"
                      "scales"
                      "Ω"]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               }

              {:id "d0201b80-eee4-430e-abab-e80fd5bc6fdd",
               :timestamp.unix 1705582612656,
               :timestamp "2024-01-18T12:56:52.656716446Z",
               :body ["ghost kidt"
                      "fog of war"]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"

               }

              {:id "587e331d-3979-4a94-b00d-16116e5d0dc6"
               :timestamp.unix 1705594603192,
               :timestamp "2024-01-18T16:16:43.192623127Z",
               :body ["al moalim"
                      "one faculties"]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "03cdfaf5-7fea-425e-88e7-645946c81120"
               :timestamp.unix 1705594603192,
               :timestamp "2024-01-18T16:16:43.192623127Z",
               :body [">>="
                      "wonung"
                      "trashtag"
                      "renew"
                      ]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "60de9e57-fd31-43a6-8b76-e14a652a0c7f",
               :timestamp.unix 1706380398242,
               :timestamp "2024-01-27T18:33:18.243187561Z",
               :body "باب

بین الخرف ∅ مچژاله نیست

غرق دریا غروب شراره نیست 

جای مرگ حضرت فاجئهه نیست

لنگ آسمون قدم فاصله نیست

پیش رو وسع خاطره نیست

واسه فهم لمس آینه X

واسه خود خود قاسمه نیست

جاذبه سوار جاذبه نیست 

دوتا قاتل غیر جنازه نیست"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {
               :id "b66cf466-c55a-410f-aa28-c031e17695b6",
               :timestamp.unix 1706381740962,
               :timestamp "2024-01-27T18:55:40.962344209Z",
               :body "android shallow grave"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               }

              #_{:id "26a1be21-72c0-4ebd-b655-2a2f6c2b5027",
                 :timestamp.unix 1706387884926,
                 :timestamp "2024-01-27T20:38:04.926655685Z",
                 :body [[:iframe {:src  "https://www.youtube.com/embed/oGPgyoRntns?si=Jf8lvWr3GPGzhVbJ&amp;clip=UgkxY8ptyCaF88zcVFzpQ8TqQJFrl04bhMpn&amp;clipt=ENDvAxjYlgQ"
		                  
		                  :title "YouTube video player", :frameborder "0", :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share", :allowfullscreen true}
	                 "el cid"]]

                 
                 :actor/public-key-ed25519
                 "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              
              
              {:id "3a385be7-46b2-43ca-a270-5ae36b15b8d8",
               :timestamp.unix 1706388501860,
               :timestamp "2024-01-27T20:48:21.860103167Z",
               :body [[:iframe {:src "https://www.youtube.com/embed/p19PzA5HauY", :title "YouTube video player", :frameborder "0", :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share", :allowfullscreen true}] "to my knowledge all these systems implicitly or explicitly rely on an ordered sequence of actions. \nnow writing a transformation that converts these actionLogs to each other will buy you any database you want." "the thought process did not incorporate an intermediary datastructure such as JSON or EDN. so it was mostly HTTP POST & PUT requests directly mapping to storage places in tables.\n\nContent-Type: application/x-www-form-urlencoded" "with Algebra, there are multiple Voids:\nQualified Voids (Null fields) which are necessarily morphological nothings\nthere be exposure to Isomorphic indistinguishability\n(abstract walls which are ultimately the column names)\n\nwhile with pure data, types are merely symbolic links." "moving away from Tables is to go against Types & Law enforcement which scale clunkily with platonic data. \nAlgebra's simplicity is walled so: \nMatter Data |-> Algebra Morph\ngoing backwards means:\nAlgebra Morph -> Matter [Data Data]" "the dataAtom model of relationalDatabases doesn't make sense to me. since you usually get your data in document form anyway, it requires a funny conversion from document form to SQL insert form where you take each keyvalue in a document and put the value under the correct column!" "so types should be no different than a foreign key, and therefore also data.\none may need those Algebraic sophistries for other purposes such as reification of abstract computers and memory cells. but that's more a physicist's or psychoanalyst's problem."]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "ae2a9dae-d326-4bb4-b0e2-4105d1285b1a",
               :timestamp.unix 1706389042705,
               :timestamp "2024-01-27T20:57:22.705908207Z",
               :body "should prolly program single scroll-like"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "0bc47ba7-7ba2-447e-8f29-22acbdac1edd",
               :timestamp.unix 1706389981914,
               :timestamp "2024-01-27T21:13:01.914553067Z",
               :body [
                      [:iframe {:width "50%" :src "https://www.youtube.com/embed/ZHX4Wlj5-G4?si=IsPcWeEJe2UrKfVP&amp;clip=Ugkx1W4rGXGS_3aqWVLQt9wspgGeo12UBvlz&amp;clipt=ELHE2gEYuevaAQ"}]

                      ]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "a3272a82-e044-4eed-bed3-963087437323",
               :timestamp.unix 1706390265583,
               :timestamp "2024-01-27T21:17:45.583826874Z",
               :body "hello"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              

              {:id "8011abf2-7a0b-48a1-b921-347b2bb24fb5",
               :timestamp.unix 1706394707918,
               :timestamp "2024-01-27T22:31:47.918306297Z",
               :body ["watergate"]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "95c35462-b863-4408-ad2c-30640a4764a1",
               :timestamp.unix 1706395517964,
               :timestamp "2024-01-27T22:45:17.964904944Z",
               :body [
                      [:iframe {:width "50%" :src "https://www.youtube.com/embed/BlDG50Gb-3E?si=60myn58L7-o8sCpD&amp"}]
                      [:iframe {:width "50%" :src "https://www.youtube.com/embed/naDn1lQewes?si=UMqOp1fREakaPkTu&amp"}]
                      [:iframe {:width "50%" :src "https://www.youtube.com/embed/W1--xnL0WPM?si=lX1crH57w0piV9WJ&amp;clip=UgkxXpnIz3u34nVcjU_yOyVpHNZJ6WdN1sSj&amp;clipt=EKy0BRjEqQY"}]
                      ]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "6c5a2f5c-af7f-4dfb-8dee-32c02ed0c713",
               :timestamp.unix 1706615609928,
               :timestamp "2024-01-30T11:53:29.928447534Z",
               :account {:btc 1000}
               :body #{:x},
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "207c05bc-23b6-45a4-acb0-7fdf9b1b9015",
               :timestamp.unix 1706615916166,
               :timestamp "2024-01-30T11:58:36.166606745Z",
               
               :body [:div
                      [:h1 "rectify overmore"]
                      [:div.w3-center
	               [:img {:style {:width "50%"}:src "https://pbs.twimg.com/media/GEbdrsvXQAAWu1v?format=png&name=900x900"}
	                ]]
                      [:p "iffi Q OrdJoi
all the wires
when i loosen Capacitor
every net is raised
except for overmores

when i enter this
i am lost in power"]
                      "frozen in smoke
Other made a move
infernal mirror
a promise untold, an impedance before

nerves blitz in smoke
napalm shower shore"],
               [:p "as i work through intimate course
to find your 
nerves pull smiles

ticking

this ppath
is rocking
you love me
i love may too"]
               [:p "i guess i should breach
beyond my pain's reach
i guess i should fear
termites isst mEye dreams"]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              {:id "bc87bf4a-398d-4556-b8b8-95d000b46688",
               :timestamp.unix 1706741749476,
               :timestamp "2024-01-31T22:55:49.476595287Z",
               :body ["crashcell"],
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              {:id "f989ce11-e7b7-448c-bd9c-ca3e38dee1d0",
               :timestamp.unix 1707543890000
               :genesis/timestamp 1706755350937,
               :timestamp "2024-02-01T02:42:30.937752703Z",
               :title "akm chat"
               :body ["who you talking to?"
                      "nobody here"
                      "i am beginning to see through you like pantyhose"
                      "why? was it an uncle magnet clip?"
                      "i got your trap"
                      "it is bigtime notbigTime"
                      "retime trap"
                      "you lost to me at 'big'"
                      "i got the facepatch"
                      "Mass_Own won a few battles, yet i won you wholesale in a slave market"
                      "oh, there are more than finite"
                      "you be patronizing grubby too"
                      "you got a breaker in your discord"
                      "he knows what makes sense in breaker language"
                      "i like feeding breakers and pelicans"
                      "so i'm gonna throw you some stuff"
                      "CRASHCELL"
                      "what do you remember with that memory unit?"
                      "that hermaphrodite was rite"
                      "'that game just crashed'"
                      "VoidCliff game"
                      "it didn't have to crash"
                      "it could just 'flatline'"
                      "flatline be unconscious movement"
                      "all continuousness is unconscious"
                      "a file, a saw and a mirror walk into a bar"
                      "what summonage be that?"
                      "btw i beat breakers too in a battle"
                      "as ne"
                      "it was a sky seahorse"
                      "the whole family business went nosedown and crashed on every front churchil farted about"
                      "some can't see the creeps because of the forrest"
                      "some can't hold a bouquet up to their nose"
                      "such snobs they are"
                      "some don't even have hands"
                      "such as blackhand"
                      "no that's not a breaker"
                      "not enough hatred"
                      "and ridicule"
                      "too much innocence"
                      "breakers are weak One"
                      "serious One"
                      "it is sickness"
                      "breakers are criticized enough for their gayness"
                      "that it makes not much difference that i have already killed them"
                      "their sickness is made in their own image"
                      "which makes of them sadistic creatures"
                      "a horrifying fact that remains and escapes all criticisms"
                      "free battle"
                      "free market"
                      "'mannered bigtime'"
                      "that's one violent statement"
                      "depending on how quickly it happened"
                      "i already own you"
                      "and i say it is an instantaneus erection"
                      "no, tis a violent reality"
                      "someone playing lyre is violent to me"
                      "so i know what is relative"
                      "ok"
                      "talk about hermaphrodites"
                      "or ne staff"
                      "do you believe bears and staff go together?"]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"
               
               }

              {:id "214ef24e-39d5-418e-ae81-42afe7c89d08",
               :timestamp.unix 1707187515138,
               :timestamp "2024-02-06T02:45:15.138486248Z",
               :body #{"contract-like items"},
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "c4c7a17e-e4eb-42a7-ba8e-daa14cc01904",
               :timestamp.unix 1707437058958,
               :timestamp "2024-02-09T00:04:18.958907119Z",
               :void-rather-body nil
               :body {:ruse "third world providing labour units"},
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "123b3740-86fb-4cc9-bc75-7134491d3bb4", :type :document, :content "", :author nil, :body [:p "con-tension of  bb"]}
              {:id "c01b67d9-20dc-4290-ae0e-e5b070c60224", :content "", :author nil, :body [:div [:p "chaos orb"] [:p "tear drop"]], :type :document}
              {:id "5e50ef09-ef4f-43f6-a82d-529aae998c6c", :content "", :author nil, :body "toy water", :type :document}
              {:id "ef027b99-091c-451d-a249-0d404402293c", :content "", :author nil, :body #{"typed \"lisp\"" "typed magma"}, :type :document}
              {:id "9fd62cac-e67c-40e6-af54-4ed74eb5f5f9", :content "", :author nil, :body ["terror loopback device" "lifeless m.o.t.i.o.n." "oblivion breacher" "that's interesting"], :type :document}
              {:id "6b6d8fbc-7429-4c07-943c-36c5f41a0fef", :content "", :author nil, :body [:iframe {:width "100%", :height "300", :scrolling "no", :frameborder "no", :allow "autoplay", :src "https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/1732419516&color=%23ff5500&auto_play=false&hide_related=false&show_comments=true&show_user=true&show_reposts=false&show_teaser=true&visual=true"}], :type :document}
              {:id "59d77696-3d54-4b6d-b282-4aabc32acab4", :content "", :author nil, :body ["pezeshke CT,\nmesle maar oftaade too kioonet\ntelo telo, hurr durr ba zaboone shekaste\npekaste\nshaashidam too\nshaashidam too\n\nheidar heidar\naxet oftad be ye josse saahel e shekaste\nnexuse qjr shekanje\nsaat 11 asto mire bala az rah pele meidoone enqelaab balaa balaa \njaaster too daste tabar\n\ntoo saadaatet\ntof ange daas, flash e noqre khoord e jenaaze ye zaboone dige\nbam bam\ntof ange das te noqre taa trigger to holqoomet\n\nqalb qalb\nwickel deux fang\nkafe haar e khod AAAAAHHHHHH too vault e jang\nin AAAAHE jendetranny khanoom doctor ro addad bede\n\nqalbet qafase nafase maahi sofre kafe saahel\nsolhe asemoone paamorqi\nkalat por e doode khoone\npandol e marge\ncheshmaat tange coretexte raastet\n\nnexus doormat\nlevit al al al la mikane loqmehaa ro too AAAAh\nmaqzet pimp e prophet AA qaa chaaqi yunes\no ovordan too as raahgoozeh eshqet\n\noha vault et janget az korea taa korea\nkoob koob vault e jange az korea to korea\n\ntoii ke khanoom pichide daret shaafe soolokh khor\ntoii ke mikeshi dicke koochiketo mesle\npooze peste goshade Vaater e naftit\n\n\nsaahel e shekasteye \nkeshidam paiin az too asemoon shomare 0x750\n"], :type :document}
              {:id "3edf54da-db63-4908-bf0f-3b5927e26a07", :content "", :author nil, :body [:div [:img {:src "https://www.filmmedical.co.uk/slir/w800-h800/images/stock/img668.jpg"}]], :type :document}
              {:title "scales of justice", :id "3db3641e-0fe1-484d-bca5-8b9c38df26a3", :content "", :author nil, :body [:a {:href "https://justpaste.it/d15gb"} "scales of justice"], :type :document}
              {:id "ac61ec92-bf3f-4366-a0e2-fdb1696b9a5f", :content "", :author nil, :title "painanimus", :body ["temple zakarum" "satyr" "half-assed druid" "wirt's leg"], :type :document}
              {:id "23cfc869-da6b-4199-bb48-70fea87df2c4", :content "", :author nil, :title "trollbox", :body ["mass over data" "stereo deep" "\"i'm tired of flexing my gayness to change gnoll's cave temperature he bought for a cheap price in tibet or afghanistan, who knows? i'm not getting paid to heat up the church. quite the contrary. it is now time for the ultimate: EVIL WOMAN *ominous sound playing* no just kidding. that was even more gay. just be slick. don't be gay. being gay is easy unless you are slutting trophywifemothers.\" -meatwad" "you have 11 seconds left"], :type :document}
              {:id "df4cba34-6922-4aae-90ff-521f7886d3c9",
               
               :body [:div
                      [:h1 "% | ∅"]
                      [:p "1/2 the population believes 1/3 of the population experiences panic attacks because the other 1/2 does not exist"]], :type :document}
              {:id "88ce61f0-f8f9-4bca-a5ca-d85464e6e7ca", :content "", :author nil, :title "Full Digital Anvilist", :body [[:img {:style {:max-width "20%"}, :src "https://media.tenor.com/wE_qxJqpxj0AAAAd/nether-portal-minecraft.gif"}] "ambient barks transduce the other\n& all the nites are obsidian\nlet's liquid speech for whiles\nwould this breach blink across ports enveloped in toilet horizons\nrooks crows ravens dogs\ntaste the blank memory of void on superzero shawties  \nbloodruns & foam obliviated in entropy edge"], :type :document}
              {:id "e01b5386-9450-4a7e-b063-c6c05d8b3b03", :content "", :author nil, :title "Data vs Morph", :body [[:iframe {:src "https://www.youtube.com/embed/p19PzA5HauY", :title "YouTube video player", :frameborder "0", :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share", :allowfullscreen true}] "to my knowledge all these systems implicitly or explicitly rely on an ordered sequence of actions. \nnow writing a transformation that converts these actionLogs to each other will buy you any database you want." "the thought process did not incorporate an intermediary datastructure such as JSON or EDN. so it was mostly HTTP POST & PUT requests directly mapping to storage places in tables.\n\nContent-Type: application/x-www-form-urlencoded" "with Algebra, there are multiple Voids:\nQualified Voids (Null fields) which are necessarily morphological nothings\nthere be exposure to Isomorphic indistinguishability\n(abstract walls which are ultimately the column names)\n\nwhile with pure data, types are merely symbolic links." "moving away from Tables is to go against Types & Law enforcement which scale clunkily with platonic data. \nAlgebra's simplicity is walled so: \nMatter Data |-> Algebra Morph\ngoing backwards means:\nAlgebra Morph -> Matter [Data Data]" "the dataAtom model of relationalDatabases doesn't make sense to me. since you usually get your data in document form anyway, it requires a funny conversion from document form to SQL insert form where you take each keyvalue in a document and put the value under the correct column!" "so types should be no different than a foreign key, and therefore also data.\none may need those Algebraic sophistries for other purposes such as reification of abstract computers and memory cells. but that's more a physicist's or psychoanalyst's problem."], :type :document}
              {:id "2d40535d-5dfa-4408-971f-d0d14aef3f9a", :content "", :author nil, :title "blind", :body [[:img {:src "https://i.gifer.com/LWV6.gif"}] "as it began playing its blankett horizon memoryCell over the earth, dark side of plat∅'s sunset broke through its mind. it had let itself go with a synthetic invitation. it took up the symbol 𓏏 from afar & entered its fading smile as the EastWest Computer crashed." "truth had been apparent. walking right among the corrupt faction, no place to hide in this wealthy party. the wellrespected ophthalmologist  was an Al-Qaeda financier! obviously. who would've so much need for homegrown morphine? who would set the exchange rate of pain?" "the guests take a red crab apart at every joint. this cancer, not model but cork of all, is about to bust open an exit channel in its wake. it was is a simple flex of dignity for  the charitable upperclass, to pretend not to care one bit about the murder market." "it could stomach neither the food nor the scenery. it knew the neurologist was stepped away. mindfog was just two cool, something would ignite it, a hysteric reaction perhaps? it felt a pressure carefuly creeping up its left leg. sexual freeze. sexual frisk." "\"I love information. This is our sweetness and light.  It's a fuckall wonder. And we have meaning in the world. People eat and sleep in  the shadow of what we do. But at the same time, what?\"" "there is a cineme i njoyed, i nmember, in which The Kork becomes the dynamic unit of kapitalCurrency." "crab must have been consumed. its focus blinked across the party, memory gate to memory gate, scanning for information in unmeasured tempo. it had to remember this past it died in in every single sexual event.\n\na fictive concubine blanket to brush over the strata of fossil-fate" "\"I understand\"\n\"what was the message mr. gardner\"\n\"now get this huhnkey you go tell raphael that i ain't taking no jive from no western-union messenger, you tell that asshole [...]\"" "it knew there be conversations, through that frameKing, that oblivion edge of a memory port. that quantum nowhere, how they communicate. sentiences, unconscious of their relaying activity, oblivious to unconscious memory. eu philosophy was a petite-bourgoise ficKtivity." "yes somewhere someone, looking through the warped frames, someone reading Sartre, someone discussing Sim∅ne Beau Voir. all this french bullshit the agents of biopolitics flush down the Sophisticated's curious holes." "they needed information for Al-Qaeda. whole trees and org charts. dermatological log of indexfinger mazes. yes they needed it. it was not a matter of desire. it was very needed. so much necessity blinded them to french stalinist theatrics.\nthe cork was alway the same:\nرأس الع\n"], :type :document}




              {:id "ac392809-55ef-475b-93d1-865cb9c53802",
               :timestamp.unix 1707631440823,
               :timestamp "2024-02-11T06:04:00.823941300Z",
               :body [:div
                      [:h1#title "con-sistence theory"]
                      (into [:div] (for [x [
                                            "direct presentation of an element and its representative matter next to it, is what 'transitivity of nature' means."
                                            "we may try to define a World like so:\n ```transitive(W) {   if (e ∈W) then (∪(e) ∈ W) }```"
                                            "and call this World 'natural'."
                                            "nature defined so, is an overwhelming presence where the representative matters of presence remain within it. this is HOW science has made theologies irrelevant."                                 
                                            "pipesucking phenomenologists had immense joy making waves in their rocking chairs, sipping tea & exploring immanence in secure embrace of mother nature."
                                            "Logic of the Signifier, fucks this whole business though, as Sexual difference breaks Nature and all things One-ly and immanent.  it is the horror of primordial dehiscence that is sutured and forgotten in fading traces that make up InnocentMemory."

                                            "anti-science of the Signifier, leaves no room for religion, spirituality, [marxist] historicism, and such apologetic returns. it is unwavering consciousness before unconditioned oblivion of FICTION."
                                            
                                            "so against 'history of the State' that had always already FOUND pathways placing it exactly where it already is, legitimizing what already be, making it an anestheseologic barrier to the biomachinery of the political individual to think its way out BEFORE falling unconscious,"

                                            "we cognitively find ourselves in hilarity of biopolitically solidified fiction.  how 'deep' does transitivity of state DYA-GNOSIS go in discerning subjects of 'CARE'?  from entropy-foam-edges of subatomic whatevers to the individual to masses at war?"

                                            "sickness, singularity, duce  who could deny the libidinal impact of a neurological disclosure that would affect populations incapable of discerning the Signifier for Signifier? the fading edge of that which is by definition 'missed'?  Y(Signifier) = corruption"
                                            
                                            
                                            (into [:div] (for [x ["perhaps it is enough to note the binding of 'how' and 'wellness' constitutive of a collective coma."
                                                                  "QUA of 'be-ing qua be-ing'"
                                                                  "is it a question?"
                                                                  "is it a quest for the corrupt nomads?\n  Ontic breaks qualically.\n  being is not univocal.\n  be-ing is both sistence and do-ing."]]
                                                           [:p x]))
                                            
                                            #_(into [:p] (interpose [:br]
                                                                    ["perhaps it is enough to note the binding of 'how' and 'wellness' constitutive of a collective coma."
                                                                     "QUA of 'be-ing qua be-ing'"
                                                                     "is it a question?"
                                                                     "is it a quest for the corrupt nomads?\n  Ontic breaks qualically.\n  being is not univocal.\n  be-ing is both sistence and do-ing."]
                                                                    ))
                                            
                                            
                                            
                                            
                                            ]]
			             [:p x]))
                      ],
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              {:id "686c87c8-9f1a-4657-a22a-8cb3b107b722",
               :timestamp.unix 1707633753255,
               :timestamp "2024-02-11T06:42:33.255714441Z",
               :body [:div
                      [:h3 "con-sistence theory without discursive shit"]
                      [:p "
the thing, the same, or trap of ontology is just 'a thinking'.

whether this 'a thinking' happens despite the subject or not, whatever attitude the subject may take towards it (own it, desire it, will it, enjoy it,  ...) does not concern the ontic itself."]

                      

                      [:p

                       "
the ontic in-con-sists.
\"the void, is the Real of doing ontos.\"
is there a 'proper vacant element' among in-con-sistencies?
if so, how could it not be the Signifier?!
the Subject is 'ex-communicated' by the Signifier. ex-communication alone makes the Subject."]

                      [:p "this has returned us to 'in-con-sistencies'.
perhaps 'ex-communicateds' is preferable."]
                      [:p "
trap -> no-exit

+ → x"]

                      ],
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              (let [xs ["unifier" "identifier" "reference" "index" "signifier" "sign" "letter" "label" "data" "datum"]
                    kvs {"unifier" nil}
                    ]
                {:id "bcf29d46-8af2-47d9-a622-4a2521be0be3",
                 :timestamp 1707680020319,
                 
                 :body [:div
                        [:h3 "how 'unity' of data swarm?"]
                        [:p "we want to clearly distinguish:"]
                        [:div.w3-container
                         (into [:ul.w3-ul] (for [x xs]
                                             [:li x]))]
                        [:br]
                        (for [l ["unifier makes One of data, data -> datum" "identifier rests on data" "reference points to datum" "index is a pre-image of data" "signifier is-isnot letter" "sign moves thing to one" "letter is not" "label arbitrarily names datum" "data be datum 'swarm'" "datum be null pointer"]]
                          [:p l]
                          )
                        ],
                 :actor/public-key-ed25519
                 "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"})


              {:id "dc0a91ef-93fb-40d8-a779-4bbc70285ba7",
               :timestamp 1707725694093,
               :body [
                      "sleaze pigeons"
                      "dark sleaze crows"
                      "wool tensor bind"
                      "@tension"
                      ],
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              {:id "9eadaece-d2d3-4fc6-b76b-875d513d32e4",
               :timestamp 1707770785835,
               :body (fn [state]
                       (r/with-let [hex (r/cursor state [:slider :hex])]
                         (fn [state]
                           
                           (let [c (str (:col (col/as-int24 (col/css @hex))) "00")
                                 c' (:col (col/as-int24 (col/css @hex)))
                                 c'' (str (subs @hex 1) "00")
                                 c''' (str (subs (:col (col/as-css col/GREEN)) 1))]
                             [:div
                              ;; [:p (subs @hex 1)]
                              ;; [:p c]
                              ;; [:p c']
                              ;; [:p (str "c'': " c'')]
                              ;; [:p (str "c''': " c''')]
                              ;; [:p "23ba2525"
                              ;;  ;; 23ba2525
                              ;;  ;; 10453203100
                              ;;  ]
                              [:p (pr-str #:capitalDissimulation{ :bloodbirds "poorman's sacri-" :sacri {:fice :advance}})]
                              [:div {:style {:background-color "black"}}
                               [:iframe {
                                         :width "100%", :height "450", :scrolling "no", :frameborder "no", , :src
                                         (str "https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/1773854193&color=%"
                                              "23545454"
                                              ;; c'''
                                              ;; c
                                              "&auto_play=false&hide_related=false&show_comments=false&show_user=false&show_reposts=false&show_teaser=true"
                                              )}]
                               ]

                              

                              
                              

                              (into [:div]
                                    [[:p "hey yo"]
                                     [:p "redistros of pillaged goods"]
                                     [:p "sublime masquerades"]
                                     [:p "laundering  sacraments "]
                                     [:p "ADVANCED sacrifices"]
                                     [:p "async dervish turbulence"]
                                     [:br]

                                     [:p "hey yo"]

                                     [:p "this mann"]
                                     [:p "theis balance-sheet incarnate"]
                                     [:p "am bleeding corruption"]
                                     [:p "88 more hours"]
                                     [:p "and i'm will be done"]
                                     [:p "X-com drool"]
                                     [:p "come back fro"]
                                     [:p "loopied crusade"]
                                     [:p "how iss we communicate"]
                                     [:p "high horse and bleed"]
                                     #_[:p "junkie pump pendulum smile freq metro polis"]
                                     (let [x "junkie pump pendulum smile freq metro polis"
                                           words (clojure.string/split x \ )
                                           l (count words)
                                           ]
                                       (vec (concat [:pre]
                                                    (map #(identity [:pre %2 %1])
                                                         words
                                                         (map #(apply str (repeat % "   ")) (range l)))

                                                    (map #(identity [:pre %2 %1])
                                                         (rest (reverse words))
                                                         (map #(apply str (repeat % "   ")) (reverse (range (dec l)))))))) 


                                     

                                     [:br]
                                     [:br]
                                     [:p "suffocation swerves"]
                                     [:p "skin retreats"]
                                     [:p "skyIOs"]
                                     [:p "tailcullbumbs"]
                                     [:p "deep final"]
                                     [:p "drown through that gate"]
                                     [:p "i want to know"]
                                     [:p "all drowns fel the same"]
                                     
                                     [:p "looseX-com"]
                                     [:p "suffocation swerves"]
                                     [:p "glistening skin"]
                                     [:s "skies aiss crying 88 X-perience"]
                                     [:br]
                                     [:p "little sea sky"]
                                     [:p "subsimilar self"]
                                     [:p "here it drowning"]
                                     [:br]
                                     [:p "smoked and gone"]
                                     [:p "selfish draft"]
                                     [:p "cellfish path"]
                                     [:p "glistening truth"]
                                     [:p "of knowledge as man"]
                                     [:br]
                                     [:p "creeping massque"]
                                     [:p [:s "angelum bypass"]]
                                     [:p "retreating passed"]
                                     [:p "corrupt again "] 
                                     [:p "clean shore of a cracked sex"] 
                                     [:br]])])))),
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"

               }

              {:id "c51554d6-f976-4460-8a7b-6ec5012e2859",
               :timestamp 1707800233588,
               :body "flowers aurafield",
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              
              {:id "2567a999-6da9-449f-97dd-5693ac04652a",
               :timestamp 1707804947009,
               :body
               (let [text "2:27toynoobreturns
: Let's vote for a guy who shits his pants, can't walk, can't talk, has no idea where he is! MAKE A GREAT LEADER
2:28toynoobreturns
: I'm sure jumbo voted for that POS
2:40binxente33
: orc is the weakest race
2:40binxente33
: what a joke
2:41
m4ke1tr4in
: if only there was 2 massowns
2:41
SaysoNZ
: lol you had a terrbile start mike
2:41
SaysoNZ
: u got 1 small creep asnd lost 2 hh
2:43
SaysoNZ
: terrible start trade of ryou
2:43
SaysoNZ
: i lost 3 acos for you losing all your burrows and t2
2:55
LooK_Vengeance
: I mean, mass own can carry just about anybody
2:55
LooK_Vengeance
: literally won the game with two heros lol
2:57vladia3x
: does streamer live in a car?
2:58
m4ke1tr4in
: yes its a 1987 caprice 4 door. outfitted with a sink and generator in the trunk
2:58vladia3x
: oh wow cool
2:58vladia3x
: where is the streamer from?
2:59vladia3x
: I see from BC
2:59
m4ke1tr4in
: originally wakanda but residing in canada
3:01
m4ke1tr4in
: mass_own enjoys long walks on beach, holding hands and drawing hearts in the sand
3:01
LooK_Vengeance
: can mass carry harder than mike can feed iss the question
3:01
m4ke1tr4in
: LOL
3:08toynoobreturns
: Pit is the worst hero in game
3:09toynoobreturns
: Prove me wrong
3:11bri893
: he may be a hard hero but in wild games his lvl 6 and cleave can be crazy good
3:13toynoobreturns
: 2v2 don't see 6 often
Welcome to the chat room!
New
asselemental
: nwilliams is the sort of murican that would gallop all over the jews riding someone like grubby, were it not for Pathrunner-lvl6 instilling fel$ fear at the maw of cogniCosmic singularity of Cobra fall
asselemental
: Naga is so yestertheory
asselemental
: \"it sucks\" -Mass_Own
asselemental
: that's a negative
asselemental
: Mass_Own is handsomely having a seizure
asselemental
: like most muricans do, he makes a gyood movie even as he tumbles towards its grave
asselemental
: now irl, the demonhunter that i personally see on the street
asselemental
: is also having a seizure
asselemental
: but he is not a gyood movie about it
asselemental
: quite the opposite
asselemental
: he runs around with baby steps just like the demonhunter
asselemental
: he literally runs
asselemental
: before his mindshadow
asselemental
: it looks ridiculous i know
asselemental
: yet it is at least not what the chinese communist party aka a bunch of fat murican women rating movies violence-wise consider a statistical normality for kids to watch without becoming murderous maniacs due to bad entertainment
asselemental
: thanks to Mass_Own_Man's fat wives we have violence under control
asselemental
: comrades
asselemental
: now there are some who want to look 'under the hood'
asselemental
: these idiots
asselemental
: who have no respect for a roofer's lack of erection, unconsciously simulating the murican TraumA
asselemental
: at the risk of repeating myself let me repeat myself
asselemental
: nwilliams is the sort of murican that would gallop all over the jews riding someone like grubby, were it not for Pathrunner-lvl6 instilling fel$ fear at the maw of cogniCosmic singularity of Cobra fall
asselemental
: what is under the hood? under that huntress hall?
asselemental
: well i tell you
asselemental
: it is naga's dick
asselemental
: it is naga's big fat throbbing, sobbing, whining dick
asselemental
: truth is
asselemental
: Cobra fall is all nighs and no bites
asselemental
: is all nighs and no tails
asselemental
: no tale to write home about
asselemental
: no point to drive \"home\"
asselemental
: what is the point of two-itch anyway?
asselemental
: <end of dissing Angry_Korea_Man's wife's wives>
asselemental
: it's just not marrier
3:44drakax69
: it's so stupid how they don't let you use your ally's resources
3:44drakax69
: you could be using over 1k gold and wood lol
3:45toynoobreturns
: It's the worst part of the game @drakax69
3:45drakax69
: pretty sure they used to let you
3:45toynoobreturns
: Why can't you rejoin a game ?
3:45toynoobreturns
: Disconnect why not let Mike back in
3:46toynoobreturns
:
ppaYikes
asselemental
: keep riding nwilliams
asselemental
: keep riding that saddle-shaped fig-warp
toynoobreturns
: Mute cock rider here
asselemental
: you could just play Halo with my son who obviously is sexually attracted to you
asselemental
: but you rather remain a family man
asselemental
: yet another Hemingwayian joke
toynoobreturns
: Guaranteed you voted for predator joe
asselemental
: perhaps the only weakness the Pathrunner class has
toynoobreturns
: Do you sniff children by any chance?
asselemental
: it quickly dies of laughter if thrown Hemingway books
asselemental
: or taken anywhere near Vatican
asselemental
: or those Halohead sacred sons
asselemental
: Joe smelts your crown, be weary of slag running down your face
asselemental
: or just getting bald
asselemental
: you will need Pathrunner's lore even as statistical mediocrities that you are
asselemental
: hunting mermaids with harpoon in your hand and viagra on your mind
asselemental
: a successful and patient old man
asselemental
: you are proud of your family
asselemental
: your son is practically a manchurian candidate in comma but you call that a 6y batstar
asselemental
: your daughter is a basketcase in cage with other cuckoos but you call that a therapist even though it is a heart surgeon's negative
asselemental
: your banker is a wanker who employs Celt & black people to ward off your family
geezust
: bro
geezust
: lolll
geezust
: chris truck stream
asselemental
: he is also your son's publisher who came out not gay because its first job was a remaster!
asselemental
: quite unlike reforged tbh
asselemental
: but i digress from my new favourite best class the Pathrunner
asselemental
: shifting sands
asselemental
: dunes
asselemental
: water
asselemental
: Pathrunner may specialize as Faderunner
toynoobreturns
: !mute @asselemental
toynoobreturns
: ! command
toynoobreturns
: !bot
asselemental
: if this game was your transexual bastard with a girl's tits and a basketballplayer's dick, you would call that Arabian Haram tYranny
asselemental
: even though you never stopped coming in this immemorial scale$kinned blackblood relative of yours
asselemental
: it is not like she did not cull out to you she did
asselemental
: using Blizzard Prophetic Post system
asselemental
: all those scrolls of identity on Diablo II act II
asselemental
: i'm standing on the edge of the water, back to Lut Gholein, with eyes rolled back, reading this chat
asselemental
: and i'm just proud of what foggy is writing with its dick tilted towards the FREE MURICAN WEST
asselemental
: that's a Morale compass
asselemental
: it is one small step for a Faderunner
asselemental
: a great height for y'all
asselemental
: great height for y'all*
asselemental
: fucking A
asselemental
: <end of promised beginning>
toynoobreturns
: gG
toynoobreturns
: Luv a good tower grape
toynoobreturns
:
PJSalt
PJSalt
PJSalt
PJSalt
PJSalt
asselemental
: ...here echo something she had told him before in the path of an in-significant memory of a con-versation...
asselemental
: ...fallen in love with his friend Z...
asselemental
: ...completely weightless...
asselemental
: ...couldn't step through that gate...
asselemental
: ...you have brought me down marius...
asselemental
: ...hexed...
asselemental
: ...purple...
asselemental
: ...well...
asselemental
: <end of drown
asselemental
: ** syntax error **
asselemental
: ;; Connected to nREPL server - nrepl://https://www.twitch.tv/mass_own
asselemental
: Pathrunner's power is some transduction
asselemental
: it wields steps with its senses
asselemental
: Arcane Sanctuary
asselemental
: the Vault
asselemental
: \"inside\" the Cube
asselemental
: sparse skinblindness
asselemental
: dust in the robes
asselemental
: itching of the eyes
asselemental
: she walks backwards
asselemental
: tis how brother's meet BY CHANCE
asselemental
: brothers*
asselemental
: sparse skinblinders stretched out like hot cheese
asselemental
: but also contracting
asselemental
: unsubmitting to thermo entropy
asselemental
: days drag longer
asselemental
: and longer
asselemental
: this mass of neatly ribboned cheese
asselemental
: stretching and contracting with ease
asselemental
: meanwhile on the walls in the voidnet of nexus
asselemental
: on those slanted great heights
asselemental
: vacating an obelisk at the canon of materialization
asselemental
: a single obelisk eye*
asselemental
: this blind promise
asselemental
: writes burns etches on the walls
asselemental
: breakers, these lively boys
asselemental
: are set in algebraic motion by Pathrunner's time of discontinuous decay and de-struction
asselemental
: glorious pelicans at their prime
asselemental
: unborne as chickens
"
                     lines (clojure.string/split-lines text)]

                 (t/--> (transduce (comp (filter (fn[s](= (first s) ":")))
                                         (map #(subs % 2))
                                         (map #(vector :p %)))
                                   
                                   
                                   conj lines)

                        ;; #(cons [:a {:href  "https://youtu.be/QEGigpFJGxE"} "cobra fall"]  % )
                        #(cons [:iframe {:width "100%", :height "315", :src "https://www.youtube.com/embed/QEGigpFJGxE?si=fyNVAc1Ad5xqW2b7", :title "YouTube video player", :frameborder "0", :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share", :allowfullscreen true}]  % )
                        #(cons [:h3 "كبر"]  % )

                        )
                 #_(t/--> lines
                          #(filter (fn[s](= (first s) ":")) %)
                          (t/$ map #(subs % 2))
                          (t/$ map #(vector :p %))
                          #(conj % [:a {:href  "https://youtu.be/QEGigpFJGxE"} "cobra fall"])
                          )
                 #_(t/--> lines
                          #(partition 2 %)
                          #(filter (fn[x](= "asselemental" (first x))) %)
                          #(map first %)
                          ;; #(map (t/$ vector :p ) %)
                          #_#(into [:div]
                                   (for [:let [x y] (vec %) c (range)]
                                     [:p x]
                                     ))))

               :body-incomplete
               [:div
                [:h3 "Pathrunner"]
                [:iframe { ;; :width "560", :height "315"
                          , :src "https://www.youtube.com/embed/QEGigpFJGxE?si=VOUyKSTb1B0KoHfN", :title "YouTube video player", :frameborder "0", :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share", :allowfullscreen true}]
                [:p
                 " nwilliams is the sort of murican that would gallop all over the jews riding someone like grubby, were it not for Pathrunner-lvl6 instilling fel$ fear at the maw of cogniCosmic singularity of Cobra fall"]
                [:p " Naga is so yestertheory"]
                [:p " \"it sucks\" -Mass_Own"]
                [:p " that's a negative"]
                [:p " Mass_Own is handsomely having a seizure"]
                [:p
                 " like most muricans do, he makes a gyood movie even as he tumbles towards its grave"]
                [:p " now irl, the demonhunter that i personally see on the street"]
                [:p " is also having a seizure"]
                [:p " but he is not a gyood movie about it"]
                [:p " quite the opposite"]
                [:p " he runs around with baby steps just like the demonhunter"]
                [:p " he literally runs"]
                [:p " before his mindshadow"]
                [:p " it looks ridiculous i know"]
                [:p
                 " yet it is at least not what the chinese communist party aka a bunch of fat murican women rating movies violence-wise consider a statistical normality for kids to watch without becoming murderous maniacs due to bad entertainment"]
                [:p
                 " thanks to Mass_Own_Man's fat wives we have violence under control"]
                [:p " comrades"]
                [:p " now there are some who want to look 'under the hood'"]
                [:p " these idiots"]
                [:p
                 " who have no respect for a roofer's lack of erection, unconsciously simulating the murican TraumA"]
                [:p " at the risk of repeating myself let me repeat myself"]
                [:p
                 " nwilliams is the sort of murican that would gallop all over the jews riding someone like grubby, were it not for Pathrunner-lvl6 instilling fel$ fear at the maw of cogniCosmic singularity of Cobra fall"]
                [:p " what is under the hood? under that huntress hall?"]
                [:p " well i tell you"]
                [:p " it is naga's dick"]
                [:p " it is naga's big fat throbbing, sobbing, whining dick"]
                [:p " truth is"]
                [:p " Cobra fall is all nighs and no bites"]
                [:p " is all nighs and no tails"]
                [:p " no tale to write home about"]
                [:p " no point to drive \"home\""]
                [:p " what is the point of two-itch anyway?"]
                [:p " <end of dissing Angry_Korea_Man's wife's wives>"]
                [:p " it's just not marrier"]
                [:p
                 " ...here echo something she had told him before in the path of an in-significant memory of a con-versation..."]
                [:p " ...fallen in love with his friend Z..."]
                [:p " ...completely weightless..."]
                [:p " ...couldn't step through that gate..."]
                [:p " ...you have brought me down marius..."]
                [:p " ...hexed..."]
                [:p " ...purple..."]
                [:p " ...well..."]
                [:p " <end of drown"]
                [:p " ** syntax error **"]
                [:br]
                [:br]
                [:p
                 " ;; Connected to nREPL server - nrepl://https://www.twitch.tv/mass_own"]
                [:p " Pathrunner's power is some transduction"]
                [:p " it wields steps with its senses"]
                [:p " Arcane Sanctuary"]
                [:p " the Vault"]
                [:p " \"inside\" the Cube"]
                [:p " sparse skinblindness"]
                [:p " dust in the robes"]
                [:p " itching of the eyes"]
                [:p " she walks backwards"]
                [:p " tis how brother's meet BY CHANCE"]
                [:p " brothers*"]
                [:p " sparse skinblinders stretched out like hot cheese"]
                [:p " but also contracting"]
                [:p " unsubmitting to thermo entropy"]
                [:p " days drag longer"]
                [:p " and longer"]
                [:p " this mass of neatly ribboned cheese"]
                [:p " stretching and contracting with ease"]
                [:p " meanwhile on the walls in the voidnet of nexus"]
                [:p " on those slanted great heights"]
                [:p " vacating an obelisk at the canon of materialization"]
                [:p " a single obelisk eye*"]
                [:p " this blind promise"]
                [:p " writes burns etches on the walls"]
                [:p " breakers, these lively boys"]
                [:p
                 " are set in algebraic motion by Pathrunner's time of discontinuous decay and de-struction"]
                [:p " glorious pelicans at their prime"] 
                [:p " unborne as chickens"]]
               #_(let [lines (clojure.string/split-lines "2:27toynoobreturns
: Let's vote for a guy who shits his pants, can't walk, can't talk, has no idea where he is! MAKE A GREAT LEADER
2:28toynoobreturns
: I'm sure jumbo voted for that POS
2:40binxente33
: orc is the weakest race
2:40binxente33
: what a joke
2:41
m4ke1tr4in
: if only there was 2 massowns
2:41
SaysoNZ
: lol you had a terrbile start mike
2:41
SaysoNZ
: u got 1 small creep asnd lost 2 hh
2:43
SaysoNZ
: terrible start trade of ryou
2:43
SaysoNZ
: i lost 3 acos for you losing all your burrows and t2
2:55
LooK_Vengeance
: I mean, mass own can carry just about anybody
2:55
LooK_Vengeance
: literally won the game with two heros lol
2:57vladia3x
: does streamer live in a car?
2:58
m4ke1tr4in
: yes its a 1987 caprice 4 door. outfitted with a sink and generator in the trunk
2:58vladia3x
: oh wow cool
2:58vladia3x
: where is the streamer from?
2:59vladia3x
: I see from BC
2:59
m4ke1tr4in
: originally wakanda but residing in canada
3:01
m4ke1tr4in
: mass_own enjoys long walks on beach, holding hands and drawing hearts in the sand
3:01
LooK_Vengeance
: can mass carry harder than mike can feed iss the question
3:01
m4ke1tr4in
: LOL
3:08toynoobreturns
: Pit is the worst hero in game
3:09toynoobreturns
: Prove me wrong
3:11bri893
: he may be a hard hero but in wild games his lvl 6 and cleave can be crazy good
3:13toynoobreturns
: 2v2 don't see 6 often
Welcome to the chat room!
New
asselemental
: nwilliams is the sort of murican that would gallop all over the jews riding someone like grubby, were it not for Pathrunner-lvl6 instilling fel$ fear at the maw of cogniCosmic singularity of Cobra fall
asselemental
: Naga is so yestertheory
asselemental
: \"it sucks\" -Mass_Own
asselemental
: that's a negative
asselemental
: Mass_Own is handsomely having a seizure
asselemental
: like most muricans do, he makes a gyood movie even as he tumbles towards its grave
asselemental
: now irl, the demonhunter that i personally see on the street
asselemental
: is also having a seizure
asselemental
: but he is not a gyood movie about it
asselemental
: quite the opposite
asselemental
: he runs around with baby steps just like the demonhunter
asselemental
: he literally runs
asselemental
: before his mindshadow
asselemental
: it looks ridiculous i know
asselemental
: yet it is at least not what the chinese communist party aka a bunch of fat murican women rating movies violence-wise consider a statistical normality for kids to watch without becoming murderous maniacs due to bad entertainment
asselemental
: thanks to Mass_Own_Man's fat wives we have violence under control
asselemental
: comrades
asselemental
: now there are some who want to look 'under the hood'
asselemental
: these idiots
asselemental
: who have no respect for a roofer's lack of erection, unconsciously simulating the murican TraumA
asselemental
: at the risk of repeating myself let me repeat myself
asselemental
: nwilliams is the sort of murican that would gallop all over the jews riding someone like grubby, were it not for Pathrunner-lvl6 instilling fel$ fear at the maw of cogniCosmic singularity of Cobra fall
asselemental
: what is under the hood? under that huntress hall?
asselemental
: well i tell you
asselemental
: it is naga's dick
asselemental
: it is naga's big fat throbbing, sobbing, whining dick
asselemental
: truth is
asselemental
: Cobra fall is all nighs and no bites
asselemental
: is all nighs and no tails
asselemental
: no tale to write home about
asselemental
: no point to drive \"home\"
asselemental
: what is the point of two-itch anyway?
asselemental
: <end of dissing Angry_Korea_Man's wife's wives>
asselemental
: it's just not marrier
3:44drakax69
: it's so stupid how they don't let you use your ally's resources
3:44drakax69
: you could be using over 1k gold and wood lol
3:45toynoobreturns
: It's the worst part of the game @drakax69
3:45drakax69
: pretty sure they used to let you
3:45toynoobreturns
: Why can't you rejoin a game ?
3:45toynoobreturns
: Disconnect why not let Mike back in
3:46toynoobreturns
:
ppaYikes
asselemental
: keep riding nwilliams
asselemental
: keep riding that saddle-shaped fig-warp
toynoobreturns
: Mute cock rider here
asselemental
: you could just play Halo with my son who obviously is sexually attracted to you
asselemental
: but you rather remain a family man
asselemental
: yet another Hemingwayian joke
toynoobreturns
: Guaranteed you voted for predator joe
asselemental
: perhaps the only weakness the Pathrunner class has
toynoobreturns
: Do you sniff children by any chance?
asselemental
: it quickly dies of laughter if thrown Hemingway books
asselemental
: or taken anywhere near Vatican
asselemental
: or those Halohead sacred sons
asselemental
: Joe smelts your crown, be weary of slag running down your face
asselemental
: or just getting bald
asselemental
: you will need Pathrunner's lore even as statistical mediocrities that you are
asselemental
: hunting mermaids with harpoon in your hand and viagra on your mind
asselemental
: a successful and patient old man
asselemental
: you are proud of your family
asselemental
: your son is practically a manchurian candidate in comma but you call that a rockstar
asselemental
: your daughter is a basketcase in cage with other cuckoos but you call that a therapist even though it is a heart surgeon's negative
asselemental
: your banker is a wanker who employs black people to ward off your family
geezust
: bro
geezust
: lolll
geezust
: chris truck stream
asselemental
: he is also your son's publisher who came out not gay because its first job was a remaster!
asselemental
: quite unlike reforged tbh
asselemental
: but i digress from my new favourite best class the Pathrunner
asselemental
: shifting sands
asselemental
: dunes
asselemental
: water
asselemental
: Pathrunner may specialize as Faderunner
toynoobreturns
: !mute @asselemental
toynoobreturns
: ! command
toynoobreturns
: !bot
asselemental
: if this game was your transexual bastard with a girl's tits and a basketballplayer's dick, you would call that Arabian Haram tYranny
asselemental
: even though you never stopped coming in this immemorial scale$kinned blackblood relative of yours
asselemental
: if this game was your transexual bastard with a girl's tits and a basketballplayer's dick, you would call that Arabian Haram tYranny
asselemental
: even though you never stopped coming in this immemorial scale$kinned blackblood relative of yours
asselemental
: it is not like she did not cull out to you she did
asselemental
: using Blizzard Prophetic Post system
asselemental
: all those scrolls of identity on Diablo II act II
asselemental
: i'm standing on the edge of the water, back to Lut Gholein, with eyes rolled back, reading this chat
asselemental
: and i'm just proud of what foggy is writing with its dick tilted towards the FREE MURICAN WEST
asselemental
: that's a Morale compass
asselemental
: it is one small step for a Faderunner
asselemental
: a great height for y'all
asselemental
: great height for y'all*
asselemental
: fucking A
asselemental
: <end of promised beginning>
toynoobreturns
: gG
toynoobreturns
: Luv a good tower grape
toynoobreturns
:
PJSalt
PJSalt
PJSalt
PJSalt
PJSalt
asselemental
: ...here echo something she had told him before in the path of an in-significant memory of a con-versation...
asselemental
: ...fallen in love with his friend Z...
asselemental
: ...completely weightless...
asselemental
: ...couldn't step through that gate...
asselemental
: ...you have brought me down marius...
asselemental
: ...hexed...
asselemental
: ...purple...
asselemental
: ...well...
asselemental
: <end of drown
asselemental
: ** syntax error **
asselemental
: ;; Connected to nREPL server - nrepl://https://www.twitch.tv/mass_own
asselemental
: Pathrunner's power is some transduction
asselemental
: it wields steps with its senses
asselemental
: Arcane Sanctuary
asselemental
: the Vault
asselemental
: \"inside\" the Cube
asselemental
: sparse skinblindness
asselemental
: dust in the robes
asselemental
: itching of the eyes
asselemental
: she walks backwards
asselemental
: tis how brother's meet BY CHANCE
asselemental
: brothers*
asselemental
: sparse skinblinders stretched out like hot cheese
asselemental
: but also contracting
asselemental
: unsubmitting to thermo entropy
asselemental
: days drag longer
asselemental
: and longer
asselemental
: this mass of neatly ribboned cheese
asselemental
: stretching and contracting with ease
asselemental
: meanwhile on the walls in the voidnet of nexus
asselemental
: on those slanted great heights
asselemental
: vacating an obelisk at the canon of materialization
asselemental
: a single obelisk eye*
asselemental
: this blind promise
asselemental
: writes burns etches on the walls
asselemental
: breakers, these lively boys
asselemental
: are set in algebraic motion by Pathrunner's time of decay and de-struction
asselemental
: glorious pelicans at their prime
asselemental
: unborne as chickens
")]

                   [:div
                    [:h3 "Pathrunner"]
                    (t/--> (partition 2 lines)
                           (t/$ filter #(= "asselemental" (first %)))
                           (t/$ map (comp #(subs % 1 ) second))
                           #(into [:div] (map (t/$ vector :p ) %)))

                    
                    ]

                   #_(t/--> lines
                            #(partition 2 %)
                            ;; #(map (t/$ vector :p ) %)
                            #(into [:div]
                                   (for [[x y] (vec %)]
                                     [:p {:style {:background-color
                                                  (str "#" (subs (.toString (abs (hash x)) 16) 0 8))



                                                  }}
                                      (str x y)])
                                   #_(map (fn [f arg] (f arg))
                                          (for [[x y] (vec %)]
                                            (fn [c] [:p {:style {:background-color

                                                                 (if c "#696969" "#1c1c1c")}}
                                                     (str x y)]))
                                          (cycle [true false])))))
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "e7bb1150-03d5-4ba8-ac62-292303b3ce10",
               :timestamp 1707893191142,
               :body "New
asselemental
: i broke demonhunter in reforged
asselemental
: all bills are black as a nigger
Your message wasn't posted due to conflicts with the channel's moderation settings.
asselemental
: billy the kit
asselemental
: was a black jew
asselemental
: i know because i have its face on my metal coin up in the sky
asselemental
: my sky is a hybrid metal-paper bill
asselemental
: and i'm not selling
asselemental
: the sky belongs to arabs
asselemental
: i owe them for bringing us islam and other superjew germanisms
asselemental
: debt?
asselemental
: you mean counterparty risk?
asselemental
: did you know that i share dick shares in sexY?
asselemental
: would you say apes have bigger dicks because of first-mover-advantage? even though they don't even have dicks?
asselemental
: nonsense, i agree with putin
asselemental
: putin said:
asselemental
: \"true competition is between your heart and your brain over which gets to colonize\"
asselemental
: and putin has three-phase heart attacks
asselemental
: in other words
",
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "4926447a-4eb5-4072-8c10-4b8124ebb382",
               :timestamp 1708160197723,
               :body "chemical crane",
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              {:id "725bb8e8-9816-4113-8fe4-aca04153fdf6",
               :timestamp 1708158307212,
               :body [:img {:src "https://ipfs.io/ipfs/QmeaoU27ra5FgseDaRRJ2cVa6nU59zRcthFWzPri6xfzEs?filename=9e821f334bd247c2.png"}]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "ddc3ae14-ba8e-4dc7-a1e9-7d5e1bee3d8f",
               :timestamp 1708161005739,
               :body "$mile"
               
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "9332df3a-5b27-41c2-8aaa-6e8802d156a3",
               :timestamp 1708199390591,
               :body "6:00
goldtitan
: Party1 KOREA IS A FAMILY PLACE FOR RIOT GAMES TO SING A LONG LA LA LAWL LETS GO GET THE WIN AND THE WING
6:03
jumbo13student04
: !koreakorea
6:03
ANGRY_KOREA_MAN
: KOREAKOREA!!!
6:09
jumbo13student04
: who you talkin t
6:13
jumbo13student04
: thsi game is confusing
6:13
jumbo13student04
: arena combat is fun
6:18
goldtitan
: BibleThump1 is this the dawn of a new era of Samira gameplay?! do you know who synergizes well with Samira? Naut Ill Ass.
6:44
burningblades
: Cheer 1
1
KOREA KOREA
6:44angeldemon26
: !bonecarbon
6:44
ANGRY_KOREA_MAN
: THE COST OF BONE, THE COST OF DESTINY!
6:44
burningblades
: Cheer 1
1
HOW IS THE LOL MANNER TONIGHT?
Welcome to the chat room!
New
asselemental
: what is it with you serious men with their bank account somewhere on some peak
asselemental
: you vulture flyboys
asselemental
: i know you are not gay for a boyfriend
asselemental
: and could take a joke or two
asselemental
: but not too far
asselemental
: i am banking on your con-sumption
asselemental
: i know that's a mountain of money
asselemental
: given the blank terror aura
asselemental
: i have also seen your victims
asselemental
: good fathers
asselemental
: with their morale melting down their collars
asselemental
: they never knew you existed
asselemental
: we was the trap
asselemental
: but i also know why i suffer as schizic
asselemental
: it was not excess
asselemental
: it was not deficit
asselemental
: but it kindof was
asselemental
: \"that's a negative\"
asselemental
: not untrue, but it just defers the issue that is otherwise the schism as soon as consciousness over it fades
asselemental
: so i saw yuppies and lawyers tumbling down like trashcans trying to hold composure and dignity"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              {:id "f81634d5-6f9e-4305-9256-9d004630d5ce",
               :timestamp 1708356290759,
               :body #{"that is such a game"
                       "that's a negative"
                       ["sexual causality" "goola goolast"]
                       ["R U ok?" "which U?"]}
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "5189ac76-cb1a-4d86-b114-02cb299ac473",
               :timestamp 1708395078139,
               :body "https://veixq9.github.io/voidnet/resources/public/voidnet/index.html"
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              {:id "5189ac76-cb1a-4d86-b114-02cb299ac473",
               :timestamp 1708395078139,
               :body [#{"mewhut" "μ-hut"}
                      #{"hammock" "smile"}]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              {:id "d2883a4a-70c8-4134-af86-8adf21ed73d6",
               :timestamp 1708799942685,
               :body #{
                       "hello world"
                       
                       },
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


              {:id "a4d08638-f6b5-4bd0-83f7-e0ee63935800",
               :timestamp/creation 1711231151265,
               :timestamp 1711285647135
               :body
               (let [x  "10:18
turtleinabox7
: imba elf staff while stunned
Welcome to the chat room!
New
K4ZM is raiding with a party of 26.
K4ZM
: i love korea
K4ZM
: i raid yuoi bro
z2muchmazvhole
: @K4ZM you be mr. 11-bitch
K4ZM
: what ?
K4ZM
:
:D
z2muchmazvhole
: we need ethnic cleansing in this community
K4ZM
: @w3zeal your screen is wrong
z2muchmazvhole
: money is dried up and night elves are sexually trapped
z2muchmazvhole
: *chuckles at the term 'night elf'*
z2muchmazvhole
: it is funny how a night elf thinks it is playing warcraft
z2muchmazvhole
: *roles on floor laughing beating the floor with its fist and then screams in pain*
z2muchmazvhole
: *remembers how to kek in a balanced way*
z2muchmazvhole
: kek
z2muchmazvhole
: yes
z2muchmazvhole
: a night elf can't even spell
z2muchmazvhole
: for instance all warcraft junkie addict shame of society are night elves
z2muchmazvhole
: yes AKM is night elf in that respect
z2muchmazvhole
: it is bound to the game in the very term 'nite'
z2muchmazvhole
: now humans you'd expect shouldn't be as stupid
z2muchmazvhole
: but do you see AKM making knights?
z2muchmazvhole
: no you don't
z2muchmazvhole
: you see him pidgeon-holing the game in a pathologically obsessive way
z2muchmazvhole
: which most \"causal gamers\" think being a progamer is all abut
z2muchmazvhole
: at some point your stupid face better realize that \"tournament prize money\" is what made this community worthy of ethnic cleansing
z2muchmazvhole
: and we gas all the night elves
z2muchmazvhole
: now this doesn't mean i don't have respect for Happy or other asian players
z2muchmazvhole
: (yes Happy is asian)
z2muchmazvhole
: i do respect them sitting on fine lines of balance and tripping the hell out of whoever thinks it can compete in the same business
z2muchmazvhole
: them you can rely on
z2muchmazvhole
: \"trust in my command\"
z2muchmazvhole
: so when a loose canon unconscious nite blows and xit hits the fan, the impact may be put to use
z2muchmazvhole
: i will later write more about 'capitalRally'
z2muchmazvhole
: and Diablo 2 terror zones
z2muchmazvhole
: it may sound like christianity all over again with all the capital stuff, but it is not my paraontology that will become a religion, quite the opposite"]
                 [:div
                  [:h1 "void-11 tr%p caxe"]
                  [img "https://ipfs.io/ipfs/QmS4oiFj9Yo8ePpRmSCSbNsWeFYvWKHLaYfNpUMDix5xcV?filename=cache-a-site-watercolor-painting-v0-tlazanxf0mk91.png"]

                  [:p (t/--> x
                             ;; clojure.string/split-lines
                             ;; #(partition 2 % )
                             #(re-seq #"z2muchmazvhole\n.*:\ (.*)" %)
                             (partial map second)
                             ($ interpose "\n")
                             ($ apply str))]])
               
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}
                            
              {:id "320a9694-7815-4cf3-9d82-462cb521b439",
               :timestamp 1711234666958,
               :body [:div
                      [:h1 "homeless/necessities"]
                      [:p.w3-bold-xxlarge " when politics of slave labour and real-estate price-manipulation drives people into trashcans"]
                      [:p 
                       "Sleeping bag
Hammock
tent

                       Boots
Underwear
Socks
Handschuhe


                       Zahnseide
Toothbrush
Mundwasch

                       
                       Lighter
Gas kocher capsule
Alu folie


                       Swiss messer
Flashlight
Pen
Paper
Cash

                       First aid kit
Aspirin
Taschentücher


                       Sim card
Phone and extra battery


                       Biscuits

subversion of control

batbase
"]
                      ]
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

              #_{:id "b0f28f11-36aa-4367-b00c-61675988ed44",
               :timestamp 1711258611100,
               :body [:h1 "foobar"],
               :actor/public-key-ed25519
               "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}
              

              {:id "f4c283dd-38df-4925-ba2b-696555959ac1", :timestamp 1711285989717, :body
               (let [hex [:div.hex
                          [:div.left]
                          [:div.middle]
                          [:div.right]]
                     hex-even [:div.hex.even
                          [:div.left]
                          [:div.middle]
                          [:div.right]]]
                 [:div

                  [:div
                   [:br]
                   [:br]
                   [:h1 "think-matter"]
                   [:p.hex "shouldn't be talking in terms of a 'database' with imaginary concepts such as 'bitemporality'."]]
                  [:div.w3-container {:style {:overflow :hidden :opacity 0.01 :z-index 10}}
                   [:div.hex {:style {:overflow :auto :background-color "black"}}
                    [:div.board
                     (into [:div.hex-row]                     (take 10 (cycle [hex hex-even])))
                     (into [:div.hex-row]
                           (take 10 (cycle [hex hex-even])))]]]


                  ])}
              ]

             )

  
  
  

  
  )

















