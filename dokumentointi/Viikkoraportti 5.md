#Viikko 35

Viikko alkoi komentorivikäyttöliittymän päivittämisellä tukemaan tehtyä Lempel-Ziv-Welch pakkausrutiinia. Toteutuksen jälkeinen toiminnon testaus paljasti jotain ongelmia pakkauksessa käytettäessä suurempia testiaineistoja kuin mitä yksikkötestauksessa oli käytetty, joten näitä tuli testauksen yhteydessä korjattua. Lisäksi komentorivikäyttöliittymän toteuttaminen tuotti jotain ongelmia, mutta onneksi ne ratkesivat debuggerin ahkeralla käytöllä.

Seuraavana oli BitPacker toiminnallisuuden lisääminen komentorivikäyttöliittymän toiminnallisuuteen. Toiminnan testauksen kanssa kävi vastaavasti kuin LZW rutiinin kohdalla eli BitPacker:a korjattiin.

Molempien luokkien yksikkötesteihin tuli tehtyä lisätestejä näiden sekä myös muiden testauksessa löydettyjen huomioiden pohjalta.

Viikon ongelmat tarjosi Java itse. Kielessä ei ole mitään etumerkitöntä tietotyyppiä eikä sellaista ihan helposti mitenkään saa käyttöön. Tämä aiheutti ongelman LZW pakkauksessa, koska sisäisesti käytetty trie käyttää luettua tavua indeksinä. Kaikki 127 ylittävät arvot muuttuivat etumerkin omaaviksi eli negatiivisiksi joka aiheutti ohjelman kaatumisen. Näin ollen pakkaus toimii oikein kunhan syötteen arvot pysyvät enintään 7-bittisinä. Testausta ja kokeilua varten komentorivikäyttöliittymän kautta etumerkillisyys poistetaan pakkauksen yhteydessä, mutta tämä aiheuttaa sen että purku ei tuota alkuperäistä tietoa. Mielenkiintona olikin tutkia miten LZW ja BitPacker toimivat esimerkiksi pakkaamattomien bittikartta kuvien tai suoritettavien ohjelmien kohdalla.

Testauksen tuloksena LZW toteutukseen on lisätty myös sanakirjan koon rajoitus. Kävi ilmi että rajoittamaton sanakirja kasvattaa helposti hiemankin isompien tiedostojen kokoa. Tämä johtuu siitä että jos sanakirjan suurin arvo on vaikka 32-bittinen arvo niin koko tiedosto kirjoitetaan 4 tavuisena, 8 bittiä per tavu. Lisäksi tällaisen sanakirjan muodostaminen ja käyttäminen kuluttavat nopeasti paljon muistia ja hidastuvat myös käytössä. Oletuksena sanakirjan koko on 12-bittiä, mikä vaikutti olevan melko yleinen käytäntö, mutta sen voi vaihtaa 9-bitin ja 16-bitin välillä vapaasti. Tämä tuki tuli myös komentorivikäyttöliittymään helpottamaan testausta.

BitPacker seuraa omalla tavallaan tätä eli sillä voi pakata mitä vain 9-bitin ja 16-bitin väliltä.

Dokumentaatiota on päivitetty ja lisätty. Myös omien kokoelma luokkien testeihin on tullut päivitystä lisätestien muodossa.

###Testaus

Muutosta on jonkin verran edellisistä viikoista johtuen testauksessa löytyneistä ongelmista tai uuden toteutuksen teolla, joka myöskin perustui testauksen tuloksiin. Kaikkien testattavien osien rivikattavuus on tällä hetkellä 100%. Coberturen ilmoittama haarakattavuus on crypto 98%, compression 96% ja utility 96%. PIT:n mutaatiokattavuus on crypton osalta 95%, compression 92% ja utilityn 95%.
