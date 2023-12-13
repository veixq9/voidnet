(ns symautre.content-data
  (:require [symautre.doc :refer [doc]]
            [symautre.tools.core :as t :refer [-->]]
            [com.rpl.specter :as sp]
            ;; [html-to-hiccup]
            [taipei-404.html :refer [html->hiccup]]
            ))
 
"https://justpaste.it/9utnr"

(def posts
  [{:id "123b3740-86fb-4cc9-bc75-7134491d3bb4", :type :document, :content "", :author nil, :body ["con-tension of  bb"]} {:id "c01b67d9-20dc-4290-ae0e-e5b070c60224", :content "", :author nil, :body [:div [:p "chaos orb"] [:p "tear drop"]], :type :document} {:id "5e50ef09-ef4f-43f6-a82d-529aae998c6c", :content "", :author nil, :body "toy water", :type :document} {:id "ef027b99-091c-451d-a249-0d404402293c", :content "", :author nil, :body #{"typed \"lisp\"" "typed magma"}, :type :document} {:id "9fd62cac-e67c-40e6-af54-4ed74eb5f5f9", :content "", :author nil, :body ["terror loopback device" "lifeless m.o.t.i.o.n." "oblivion breacher" "that's interesting"], :type :document} {:id "6b6d8fbc-7429-4c07-943c-36c5f41a0fef", :content "", :author nil, :body [:iframe {:width "100%", :height "300", :scrolling "no", :frameborder "no", :allow "autoplay", :src "https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/1732419516&color=%23ff5500&auto_play=false&hide_related=false&show_comments=true&show_user=true&show_reposts=false&show_teaser=true&visual=true"}], :type :document} {:id "59d77696-3d54-4b6d-b282-4aabc32acab4", :content "", :author nil, :body ["pezeshke CT,\nmesle maar oftaade too kioonet\ntelo telo, hurr durr ba zaboone shekaste\npekaste\nshaashidam too\nshaashidam too\n\nheidar heidar\naxet oftad be ye josse saahel e shekaste\nnexuse qjr shekanje\nsaat 11 asto mire bala az rah pele meidoone enqelaab balaa balaa \njaaster too daste tabar\n\ntoo saadaatet\ntof ange daas, flash e noqre khoord e jenaaze ye zaboone dige\nbam bam\ntof ange das te noqre taa trigger to holqoomet\n\nqalb qalb\nwickel deux fang\nkafe haar e khod AAAAAHHHHHH too vault e jang\nin AAAAHE jendetranny khanoom doctor ro addad bede\n\nqalbet qafase nafase maahi sofre kafe saahel\nsolhe asemoone paamorqi\nkalat por e doode khoone\npandol e marge\ncheshmaat tange coretexte raastet\n\nnexus doormat\nlevit al al al la mikane loqmehaa ro too AAAAh\nmaqzet pimp e prophet AA qaa chaaqi yunes\no ovordan too as raahgoozeh eshqet\n\noha vault et janget az korea taa korea\nkoob koob vault e jange az korea to korea\n\ntoii ke khanoom pichide daret shaafe soolokh khor\ntoii ke mikeshi dicke koochiketo mesle\npooze peste goshade Vaater e naftit\n\n\nsaahel e shekasteye \nkeshidam paiin az too asemoon shomare 0x750\n"], :type :document} {:id "3edf54da-db63-4908-bf0f-3b5927e26a07", :content "", :author nil, :body [:div [:img {:src "https://www.filmmedical.co.uk/slir/w800-h800/images/stock/img668.jpg"}]], :type :document} {:title "scales of justice", :id "3db3641e-0fe1-484d-bca5-8b9c38df26a3", :content "", :author nil, :body [:a {:href "https://justpaste.it/d15gb"} "scales of justice"], :type :document} {:id "ac61ec92-bf3f-4366-a0e2-fdb1696b9a5f", :content "", :author nil, :title "painanimus", :body ["temple zakarum" "satyr" "half-assed druid" "wirt's leg"], :type :document} {:id "23cfc869-da6b-4199-bb48-70fea87df2c4", :content "", :author nil, :title "trollbox", :body ["mass over data" "stereo deep" "\"i'm tired of flexing my gayness to change gnoll's cave temperature he bought for a cheap price in tibet or afghanistan, who knows? i'm not getting paid to heat up the church. quite the contrary. it is now time for the ultimate: EVIL WOMAN *ominous sound playing* no just kidding. that was even more gay. just be slick. don't be gay. being gay is easy unless you are slutting trophywifemothers.\" -meatwad" "you have 11 seconds left"], :type :document} {:id "df4cba34-6922-4aae-90ff-521f7886d3c9", :title "P%litics", :content "", :author nil, :body ["1/2 the population believes 1/3 of the population experiences panic attacks because the other 1/2 does not exist"], :type :document} {:id "88ce61f0-f8f9-4bca-a5ca-d85464e6e7ca", :content "", :author nil, :title "Full Digital Anvilist", :body [[:img {:style {:max-width "20%"}, :src "https://media.tenor.com/wE_qxJqpxj0AAAAd/nether-portal-minecraft.gif"}] "ambient barks transduce the other\n& all the nites are obsidian\nlet's liquid speech for whiles\nwould this breach blink across ports enveloped in toilet horizons\nrooks crows ravens dogs\ntaste the blank memory of void on superzero shawties  \nbloodruns & foam obliviated in entropy edge"], :type :document} {:id "e01b5386-9450-4a7e-b063-c6c05d8b3b03", :content "", :author nil, :title "Data vs Morph", :body [[:iframe {:src "https://www.youtube.com/embed/p19PzA5HauY", :title "YouTube video player", :frameborder "0", :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share", :allowfullscreen true}] "to my knowledge all these systems implicitly or explicitly rely on an ordered sequence of actions. \nnow writing a transformation that converts these actionLogs to each other will buy you any database you want." "the thought process did not incorporate an intermediary datastructure such as JSON or EDN. so it was mostly HTTP POST & PUT requests directly mapping to storage places in tables.\n\nContent-Type: application/x-www-form-urlencoded" "with Algebra, there are multiple Voids:\nQualified Voids (Null fields) which are necessarily morphological nothings\nthere be exposure to Isomorphic indistinguishability\n(abstract walls which are ultimately the column names)\n\nwhile with pure data, types are merely symbolic links." "moving away from Tables is to go against Types & Law enforcement which scale clunkily with platonic data. \nAlgebra's simplicity is walled so: \nMatter Data |-> Algebra Morph\ngoing backwards means:\nAlgebra Morph -> Matter [Data Data]" "the dataAtom model of relationalDatabases doesn't make sense to me. since you usually get your data in document form anyway, it requires a funny conversion from document form to SQL insert form where you take each keyvalue in a document and put the value under the correct column!" "so types should be no different than a foreign key, and therefore also data.\none may need those Algebraic sophistries for other purposes such as reification of abstract computers and memory cells. but that's more a physicist's or psychoanalyst's problem."], :type :document} {:id "2d40535d-5dfa-4408-971f-d0d14aef3f9a", :content "", :author nil, :title "blind", :body [[:img {:src "https://i.gifer.com/LWV6.gif"}] "as it began playing its blankett horizon memoryCell over the earth, dark side of plat∅'s sunset broke through its mind. it had let itself go with a synthetic invitation. it took up the symbol 𓏏 from afar & entered its fading smile as the EastWest Computer crashed." "truth had been apparent. walking right among the corrupt faction, no place to hide in this wealthy party. the wellrespected ophthalmologist  was an Al-Qaeda financier! obviously. who would've so much need for homegrown morphine? who would set the exchange rate of pain?" "the guests take a red crab apart at every joint. this cancer, not model but cork of all, is about to bust open an exit channel in its wake. it was is a simple flex of dignity for  the charitable upperclass, to pretend not to care one bit about the murder market." "it could stomach neither the food nor the scenery. it knew the neurologist was stepped away. mindfog was just two cool, something would ignite it, a hysteric reaction perhaps? it felt a pressure carefuly creeping up its left leg. sexual freeze. sexual frisk." "\"I love information. This is our sweetness and light.  It's a fuckall wonder. And we have meaning in the world. People eat and sleep in  the shadow of what we do. But at the same time, what?\"" "there is a cineme i njoyed, i nmember, in which The Kork becomes the dynamic unit of kapitalCurrency." "crab must have been consumed. its focus blinked across the party, memory gate to memory gate, scanning for information in unmeasured tempo. it had to remember this past it died in in every single sexual event.\n\na fictive concubine blanket to brush over the strata of fossil-fate" "\"I understand\"\n\"what was the message mr. gardner\"\n\"now get this huhnkey you go tell raphael that i ain't taking no jive from no western-union messenger, you tell that asshole [...]\"" "it knew there be conversations, through that frameKing, that oblivion edge of a memory port. that quantum nowhere, how they communicate. sentiences, unconscious of their relaying activity, oblivious to unconscious memory. eu philosophy was a petite-bourgoise ficKtivity." "yes somewhere someone, looking through the warped frames, someone reading Sartre, someone discussing Sim∅ne Beau Voir. all this french bullshit the agents of biopolitics flush down the Sophisticated's curious holes." "they needed information for Al-Qaeda. whole trees and org charts. dermatological log of indexfinger mazes. yes they needed it. it was not a matter of desire. it was very needed. so much necessity blinded them to french stalinist theatrics.\nthe cork was alway the same:\nرأس الع\n"], :type :document}])



;; (def posts
;;   (map #(update % :timestamp (fn[t] (.toString t)))
;;        (map #(assoc % :type :document)
;;             [
;;              #_(doc {:body [""]})


        
;;              (doc {:body
;;                    ["con-tension of  bb"]})

;;              {:id "c01b67d9-20dc-4290-ae0e-e5b070c60224", :timestamp.unix 1702305747516, :timestamp #inst "2023-12-11T14:42:27.516-00:00", :content "", :author nil, :body [:div
;;                                                                                                                                                                             [:p "chaos orb"]
;;                                                                                                                                                                             [:p "tear drop"]]}
        
        
;;              {:id "5e50ef09-ef4f-43f6-a82d-529aae998c6c", :timestamp.unix 1702218094234, :timestamp #inst "2023-12-10T14:21:34.234-00:00", :content "", :author nil, :body "toy water"}

;;              {:id "ef027b99-091c-451d-a249-0d404402293c", :timestamp.unix 1702218091049, :timestamp #inst "2023-12-10T14:21:31.049-00:00", :content "", :author nil, :body #{"typed \"lisp\"" "typed magma"}}
        
;;              {:id "9fd62cac-e67c-40e6-af54-4ed74eb5f5f9", :timestamp.unix 1701666794608, :timestamp #inst "2023-12-04T05:13:14.608-00:00", :content "", :author nil, :body
;;               ["terror loopback device"
;;                "lifeless m.o.t.i.o.n."
;;                "oblivion breacher"
;;                "that's interesting"
;;                ]
         
;;               }
        

        
;;              {:id "6b6d8fbc-7429-4c07-943c-36c5f41a0fef", :timestamp.unix 1701616278142, :timestamp #inst "2023-12-03T15:11:18.142-00:00", :content "", :author nil,
;;               :body [:iframe {:width "100%", :height "300",
;;                               :scrolling "no", :frameborder "no",
;;                               :allow "autoplay",
;;                               :src "https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/1732419516&color=%23ff5500&auto_play=false&hide_related=false&show_comments=true&show_user=true&show_reposts=false&show_teaser=true&visual=true"}]}
        
;;              {:id "59d77696-3d54-4b6d-b282-4aabc32acab4", :timestamp.unix 1701599129602, :timestamp #inst "2023-12-03T10:25:29.602-00:00", :content "", :author nil,
;;               :body ["pezeshke CT,\nmesle maar oftaade too kioonet\ntelo telo, hurr durr ba zaboone shekaste\npekaste\nshaashidam too\nshaashidam too\n\nheidar heidar\naxet oftad be ye josse saahel e shekaste\nnexuse qjr shekanje\nsaat 11 asto mire bala az rah pele meidoone enqelaab balaa balaa \njaaster too daste tabar\n\ntoo saadaatet\ntof ange daas, flash e noqre khoord e jenaaze ye zaboone dige\nbam bam\ntof ange das te noqre taa trigger to holqoomet\n\nqalb qalb\nwickel deux fang\nkafe haar e khod AAAAAHHHHHH too vault e jang\nin AAAAHE jendetranny khanoom doctor ro addad bede\n\nqalbet qafase nafase maahi sofre kafe saahel\nsolhe asemoone paamorqi\nkalat por e doode khoone\npandol e marge\ncheshmaat tange coretexte raastet\n\nnexus doormat\nlevit al al al la mikane loqmehaa ro too AAAAh\nmaqzet pimp e prophet AA qaa chaaqi yunes\no ovordan too as raahgoozeh eshqet\n\noha vault et janget az korea taa korea\nkoob koob vault e jange az korea to korea\n\ntoii ke khanoom pichide daret shaafe soolokh khor\ntoii ke mikeshi dicke koochiketo mesle\npooze peste goshade Vaater e naftit\n\n\nsaahel e shekasteye \nkeshidam paiin az too asemoon shomare 0x750\n"]}
        
;;              {:id "3edf54da-db63-4908-bf0f-3b5927e26a07", :timestamp.unix 1701540891155, :timestamp #inst "2023-12-02T18:14:51.155-00:00", :content "", :author nil,
;;               :body [:div
;;                      [:img {:src "https://www.filmmedical.co.uk/slir/w800-h800/images/stock/img668.jpg"}]
                
;;                      ]}
        
        
;;              (let [this {:title "scales of justice"
;;                          :id "3db3641e-0fe1-484d-bca5-8b9c38df26a3",
;;                          :timestamp.unix 1701535196288,
;;                          :timestamp #inst "2023-12-02T16:39:56.288-00:00",
;;                          :content "", :author nil,

;;                          :body (with-meta [:a {:href "https://justpaste.it/d15gb" }] :html)}]
;;                (--> this
;;                     #(sp/transform [:body ] (fn[x](conj x (:title this))) %)
               
;;                     ))
        
        
;;              {:id "ac61ec92-bf3f-4366-a0e2-fdb1696b9a5f", :timestamp.unix 1701534514597, :timestamp #inst "2023-12-02T16:28:34.597-00:00", :content "", :author nil, :title "painanimus", :body ["temple zakarum" "satyr" "half-assed druid" "wirt's leg"]}
        


        
;;              {:id "23cfc869-da6b-4199-bb48-70fea87df2c4", :timestamp.unix 1701488482157, :timestamp #inst "2023-12-02T03:41:22.157-00:00", :content "", :author nil, :title "trollbox", :body
;;               ["mass over data"
;;                "stereo deep"
;;                "\"i'm tired of flexing my gayness to change gnoll's cave temperature he bought for a cheap price in tibet or afghanistan, who knows? i'm not getting paid to heat up the church. quite the contrary. it is now time for the ultimate: EVIL WOMAN *ominous sound playing* no just kidding. that was even more gay. just be slick. don't be gay. being gay is easy unless you are slutting trophywifemothers.\" -meatwad"
;;                "you have 11 seconds left"
;;                ]}
        
;;              {:id "df4cba34-6922-4aae-90ff-521f7886d3c9",
;;               :title "P%litics"
;;               :timestamp.unix 1701477884781, :timestamp #inst "2023-12-02T00:44:44.781-00:00", :content "", :author nil, :body ["1/2 the population believes 1/3 of the population experiences panic attacks because the other 1/2 does not exist"]}
        
;;              {:id "88ce61f0-f8f9-4bca-a5ca-d85464e6e7ca", :timestamp.unix 1701096360237, :timestamp #inst "2023-11-27T14:46:00.237-00:00",
;;               :content "",
;;               :author nil,
;;               :title "Full Digital Anvilist",
;;               :body [[:img {:style {:max-width "20%"}, :src "https://media.tenor.com/wE_qxJqpxj0AAAAd/nether-portal-minecraft.gif"}] "ambient barks transduce the other\n& all the nites are obsidian\nlet's liquid speech for whiles\nwould this breach blink across ports enveloped in toilet horizons\nrooks crows ravens dogs\ntaste the blank memory of void on superzero shawties  \nbloodruns & foam obliviated in entropy edge"]}
        
;;              {:id "e01b5386-9450-4a7e-b063-c6c05d8b3b03", :timestamp.unix 1701096360237, :timestamp #inst "2023-11-27T14:46:00.237-00:00", :content "", :author nil, :title "Data vs Morph", :body [[:iframe {:src "https://www.youtube.com/embed/p19PzA5HauY", :title "YouTube video player", :frameborder "0", :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share", :allowfullscreen true}] "to my knowledge all these systems implicitly or explicitly rely on an ordered sequence of actions. \nnow writing a transformation that converts these actionLogs to each other will buy you any database you want." "the thought process did not incorporate an intermediary datastructure such as JSON or EDN. so it was mostly HTTP POST & PUT requests directly mapping to storage places in tables.\n\nContent-Type: application/x-www-form-urlencoded" "with Algebra, there are multiple Voids:\nQualified Voids (Null fields) which are necessarily morphological nothings\nthere be exposure to Isomorphic indistinguishability\n(abstract walls which are ultimately the column names)\n\nwhile with pure data, types are merely symbolic links." "moving away from Tables is to go against Types & Law enforcement which scale clunkily with platonic data. \nAlgebra's simplicity is walled so: \nMatter Data |-> Algebra Morph\ngoing backwards means:\nAlgebra Morph -> Matter [Data Data]" "the dataAtom model of relationalDatabases doesn't make sense to me. since you usually get your data in document form anyway, it requires a funny conversion from document form to SQL insert form where you take each keyvalue in a document and put the value under the correct column!" "so types should be no different than a foreign key, and therefore also data.\none may need those Algebraic sophistries for other purposes such as reification of abstract computers and memory cells. but that's more a physicist's or psychoanalyst's problem."]}
        
;;              {:id "2d40535d-5dfa-4408-971f-d0d14aef3f9a", :timestamp.unix 1701096360237, :timestamp #inst "2023-11-27T14:46:00.237-00:00", :content "", :author nil, :title "blind", :body [[:img {:src "https://i.gifer.com/LWV6.gif"}] "as it began playing its blankett horizon memoryCell over the earth, dark side of plat∅'s sunset broke through its mind. it had let itself go with a synthetic invitation. it took up the symbol 𓏏 from afar & entered its fading smile as the EastWest Computer crashed." "truth had been apparent. walking right among the corrupt faction, no place to hide in this wealthy party. the wellrespected ophthalmologist  was an Al-Qaeda financier! obviously. who would've so much need for homegrown morphine? who would set the exchange rate of pain?" "the guests take a red crab apart at every joint. this cancer, not model but cork of all, is about to bust open an exit channel in its wake. it was is a simple flex of dignity for  the charitable upperclass, to pretend not to care one bit about the murder market." "it could stomach neither the food nor the scenery. it knew the neurologist was stepped away. mindfog was just two cool, something would ignite it, a hysteric reaction perhaps? it felt a pressure carefuly creeping up its left leg. sexual freeze. sexual frisk." "\"I love information. This is our sweetness and light.  It's a fuckall wonder. And we have meaning in the world. People eat and sleep in  the shadow of what we do. But at the same time, what?\"" "there is a cineme i njoyed, i nmember, in which The Kork becomes the dynamic unit of kapitalCurrency." "crab must have been consumed. its focus blinked across the party, memory gate to memory gate, scanning for information in unmeasured tempo. it had to remember this past it died in in every single sexual event.\n\na fictive concubine blanket to brush over the strata of fossil-fate" "\"I understand\"\n\"what was the message mr. gardner\"\n\"now get this huhnkey you go tell raphael that i ain't taking no jive from no western-union messenger, you tell that asshole [...]\"" "it knew there be conversations, through that frameKing, that oblivion edge of a memory port. that quantum nowhere, how they communicate. sentiences, unconscious of their relaying activity, oblivious to unconscious memory. eu philosophy was a petite-bourgoise ficKtivity." "yes somewhere someone, looking through the warped frames, someone reading Sartre, someone discussing Sim∅ne Beau Voir. all this french bullshit the agents of biopolitics flush down the Sophisticated's curious holes." "they needed information for Al-Qaeda. whole trees and org charts. dermatological log of indexfinger mazes. yes they needed it. it was not a matter of desire. it was very needed. so much necessity blinded them to french stalinist theatrics.\nthe cork was alway the same:\nرأس الع\n"]}
;;              ])))




