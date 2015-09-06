# Toteutus

## Yleiskuvaus
Ohjelma toteuttaa standardin mukaisen [DES (Data encryption standard)](https://en.wikipedia.org/wiki/Data_Encryption_Standard) algoritmin ja muunnelman [Lempel-Ziv-Welch (LZW)](https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch) pakkausalgoritmista. Lisäki toteutettuna on läheisesti LZW:n liittyvä bittitason pakkausalgoritmi.

Minkä tahansa algoritmin voi ottaa käyttöön omaan projektiinsa kunhan ottaa mukaan mahdollisesti tarvittavat tietorakenteet tai korvaa ne Javan tarjoamilla vastaavilla rakenteilla.

Luokka `DES.java` sisältää kaiken tarpeellisen eikä ole riippuvainen muista kuin normaaleista Javan palveluista. Luokka `LempelZivWelch.java` käyttää hyväkseen projektiin tehtyjä tietorakenteita; trie ja list. Ensimmäinen käyttää sisäisesti vielä omatekoista map kokoelmaa. Nämä voidaan ottaa mukaan projektista tai korvata Javan palveluilla. Jälkimmäisessä tapauksessa trie:n voisi korvata esimerkiksi Javan [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html) kokoelmalla ja list korvautuisi Javan [ArrayList](http://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html) kokoelmalla. `BitPacker.java` luokka on samanlainen kuin aiemmin mainittu `DES.java` eli sen voi ottaa käyttöön sellaisenaan.

Ohjelma käyttää hyväkseen joitain Javan versio kahdeksassa tulleita ominaisuuksia joten tämä täytyy ottaa huomioon luokkien käytössä tai tämän projektin kääntämisessä. Käytössä on ollut OpenJDK versio 1.8.0_45-internal. Ohjeet tämän OpenJDK:n version käytöstä Ubuntu 14.04 LTS kanssa löytyy esimerkiksi aiemmasta [MiPuz ohjelman kääntäminen](https://github.com/liquiddragon/MiPuz/blob/master/dokumentointi/ohjelmanK%C3%A4%C3%A4nt%C3%A4minen.md) dokumentaatiosta.

Projektille on tehty myös yksinkertainen komentorivikäyttöliittymä jolla voi kokeilla salausta ja pakkausta. Pakkaukseen voi myös vaikuttaa muutamalla komentorivillä annettavalla valitsemella. Komentorivikäyttöliittymä on lähinnä testausta varten eikä se välttämättä sovellu yleisempään käyttöön.

## Toiminta ja rakenne

Ohjelma jakaantuu neljään pakkaukseen; compression, crypto, utility ja cryptcomp. Pakkaamiseen liittyvä toiminnallisuus on compression pakkauksessa. Salauksen toiminnallisuus on crypto pakkauksessa. Utility pakkaus sisältää omien tietorakenteiden toteutukset. Komentorivikäyttöliittymä apuluokkineen on cryptcomp pakkauksessa.

### Luokkarakenne

Projektin pääluokkakaavio löytyy [ohesta](https://github.com/liquiddragon/CryptComp/tree/master/dokumentointi/CryptComp whole diagram.pdf) ja päätoteutuksen osalta hieman tarkempi [kaavio](https://github.com/liquiddragon/CryptComp/tree/master/dokumentointi/CryptComp class diagram.pdf).

### Pakkaus

 Compression sisältää toteutetun pakkausalgoritmin [Lempel-Ziv-Welch](https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch) eli paranneltu Lempel-Ziv 78. Algoritmin ytimessä on pakkaus, compress, ja purku, decompress, funktiot. Molemmat luovat alussa perussanakirjan 256 ensimmäisestä ASCII merkistä. Tämän jälkeen ne käyvät läpi itse pakattavan tai purettavan tiedon.
 Lempel-Ziv-Welch luokka käyttää sisäisenä tietorakenteenaan CCList ja CCTrie luokkia. Jälkimmäinen käyttää vielä apunaan CCMap luokkaa.

 Pakkauksessa käydään kaikki tiedon merkit, m, läpi eli aika vaativuus on O(m). Haettavaa alkiota etsitään trie kokoelmasta tieto kerrallaan joten keskimäärin hakuun menee O(sanakirjan koko/2) aikaa. Mikäli alkiota ei löydy se lisätään sanakirjaan johon kuluu O(sanakirjan koko) aikaa sillä lisäys tarkistaa ensin löytyikö alkiota jo sanakirjasta ja jos löytyy korvaa vanhan avaimen arvon uudella. Normaalimpi tapaus on että sitä ei löydy jolloin se lisätään sanakirjan loppuun. Sanakirjan koko kasvaa alkion kerrallaan.

 Muistia pakkauksessa sanakirjan osalta kuluu O(256 + lisättävien alkioiden lkm) muistia. Itse pakattavan tiedon tarven vaihtelee riippuen pakkauksen onnistumisesta, mutta huonossa tapauksessa pakattavan tiedon koko voi jopa kasvaa alkuperäisestä.

 Purun aika- ja muistivaatimukset ovat lähes identtiset sillä alku on sama. Purussa käydään pakattu tieto, n, läpi eli aikaa kuluu O(n). Jokaista pakatun tiedon osaa etsitään sanakirjasta johon kuluu keskimäärin O(sanakirjan koko/2) aikaa ja mikäli sitä ei sieltä löydy luodaan uusi sanakirjamerkintä kuten pakkauksessa.

 BitPacker on aputoiminto LZW:lle, mutta siitä täysin erillinen eli sitä voi käyttää myös itsenäisenä toimintonaan. BitPacker:n aikavaativuus pakkauksessa ja purusssa on keskimäärin noin O(syötteen koko) ja tilavaativuus O(syöte * 2). Muut toiminnot ovat vakioaikaisia tai -tilaisia.

 Pakkaus sisältää myös toisen luokan, BitPacker, jota voidaan käyttää myös itsenäisenä osan tiivistämään sanaa, 16-bittiä, pienempiä arvoja yhteen sanaan pakkaamalla niitä bittitasolla. Esimerkiksi 10-bittisiä arvoja voitaisiin näin ollen tiivistää 8 kappaletta 5 sanaan, 8 kpl arvoja * 10 bittiä = 80 bittiä = 5 kpl arvoja @ 16 bittiä.

 BitPacker voidaan myös yhdistää em. LZW pakkauksen pakatun muodon jatkopakkaukseen.

### Salaus

Ensimmäinen toteutus on [DES (Data encryption standard)](https://en.wikipedia.org/wiki/Data_Encryption_Standard). Algoritmin ydin, des funktio,
tekee joukon vakioaikaisia operaatiota alussa ja lopussa sekä välissä kiinteän lukumäärän kierroksia kuten standardi määrittää. Tämä funktio käsittelee vain kahdeksan tavua kerrallaan, ei siis koko viestiä. Sekä encryptBlock että decryptBlock käyttävät tätä funktiota toimintansa ytimessä. Apufunktio, encrypt ja decrypt, salaavat tai purkavat annetun tavujonon kokonaisuudessaan.

Apufunktioiden, encrypt ja decrypt, aikavaatimus on siis viestin pituus, m, eli O(m). Varsinainen ydinkfunktio, des, voidaan ajatella toimivan vakioajassa eli O(1).

Apufunktiot, encrypt ja decrypt, käyttävät annetun tavujonon, m, sekä apuna käytettävien samankokoisen tavujonojen verran tilaa eli O(3m).

Muut apufunktiot käyttävät pienempiä osuuksia aikaa tai tilaa. Esimerkiksi padding ja byte-int-byte muunnokset käsittelevät vain kahdeksaa tavua kerrallaan tehden vakioaikaisia toimintoja. Avaimen luonti merkkijonosta käyttää avaimen pituuden, ap, verran aikaa eli O(ap) ja tilaa vain kiinteän mittaisen avaimen verran eli kahdeksan tavua. DES:n osa-avainten luonnissa käydään kiinteä, standardin mukainen kierroksmäärä läpi jonka aikavaatimus on siis kierrosten lukumäärä kertaa kiinteä avaimen pituus. Muistinkulutus on kiinteä avaimen sekä apurakenteiden pituus ja jossa apurakenteiden pituudet ovat vakiot.

### Työkalut

 Utility sisältää apuna käytettävät tietorakenteet; list, map ja trie. List ja map ovat yksinkertaistettuja rakenteita Javan vastaavasta. Niiden tarkoitus on tarjota toteutus ilman Javan tarjoamia rakenteita. Trie on puumainen rakenne joka tallentaa tiedon järjestetyssä muodossa ja samalla hieman pakkaa tallennettavaa tietoa.

Map: Tilanvaraus on alussa 16 alkiota ja tilan loppuessa sen määrä kaksinkertaistuu. Kasvatus vie aikaa O(alkioiden lkm) ja muistia O(3 * alkioiden lkm). Kolminkertainen tarkoittaa alkuperäistä alkioiden lukumäärää, n, plus uutta alkioiden lukumäärää, 2*n. Alkion lisäys käy läpi olemassa olevat alkiot tarkistaakseen onko kyseessä olemassa olevan avaimen arvon muutos. Tämä vie keskimäärin O(alkioiden lkm/2) mikäli avain on olemassa. Mikäli kyseessä on uusi avain aikavaatimus on O(alkioiden lkm) plus mahdollinen kapasiteetin kasvatus ja vakioaikainen lisäys. Alkion poisto vie keskimäärin O(alkioiden lkm/2) aikaa sekä tiivistyksen joka on keskimäärin sama kuin poistokohdan löytyminen. Avaimen etsintä vie keskimäärin O(alkioiden lkm/2) aikaa. Muistia kuluu alussa 16 alkioita ja myöhemmin tämän monin kertoja kokoelman kasvaessa. Poistossa muistia ei vapauteta.

List: Tilanvaraus on alussa 10 alkiota ja tilan loppuessa sen määrä kaksinkertaistuu. Kasvatus vie aikaa O(alkioiden lkm) ja muistia O(3 * alkioiden lkm). Kolminkertainen tarkoittaa alkuperäistä alkioiden lukumäärää, n, plus uutta alkioiden lukumäärää, 2*n. Alkion lisäys on vakioaikainen operaatio p.l. mikäli tila ei riitä. Tällöin tehdään tilan kasvatus. Alkion haku vie keskimäärin O(alkioiden lkm/2) aikaa. Poisto itsessään on vakioaikainen, mutta sen yhteydessä tehtävä tiivistys on keskimäärin O(alkioiden lkm/2).

Muut operaatiot edellisissä ovat vakioaikaisia ja -tilaisia. Esimerkiksi koon selvittäminen palauttaa vain sisäisen muuttujan arvon tai listan iteraattorin seuraavan elementin tarkistus tutkii onko k.o. indeksillä olemassa elementtiä.

Trie: Sisäinen talletus käyttää node luokasta koostuvaa puumaista rakennetta, jonka koko on melko pieni p.l. viitetaulukkoa joka alustetaan oletussanakirjan kooksi eli 256 elementtiä. Myös takaperoisen haun mahdollistava taulu vie muisti trie:hen talletettujen kokonaisten alkioiden plus yhden viitealkion verran tilaa. Lisääminen vie O(lisättävän alkion mitta) aikaa ja mustia sisäinen noden luokan verran, O(sis. node). Haut, eli get ja getReverse, vievät keskimäärin O(entry / 2) aikaa ilman erillisen lisätilan vaativuutta. Myös contains toimii samoilla vaatimuksilla kuin get. Muut metodit ovat vakioaikaisia ja -tilaisia.

## Huomioitavaa

Javan tietotyypit ovat aina etumerkillisiä; [Primitive Data Types](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html). Tämä aiheuttaa ongelman ohjelman käytössä pakkauksen kohdalla sillä pakkauksessa käytetään sisäisesti pakattavan tavun arvoa indeksinä. Näin ollen tiedostossa olevat tavut joiden arvo ylittää 127 tulkitaan negatiivisiksi arvoiksi Javan toimesta. Komentorivikäyttöliittymään on tehty toiminto joka muuttaa nämä arvot etumerkittömiksi kokonaisluvuiksi [Byte.toUnsignedInt](https://docs.oracle.com/javase/8/docs/api/java/lang/Byte.html#toUnsignedInt-byte-) komennolla. Tämä mahdollistaa pakkauksen testaamisen myös binääritiedostoilla toteutuksen tehokkuuden tutkimiseksi muiden kuin tekstitiedostojen kohdalla. Haittana on että näin pakattuja tiedostoja ei voi purkaa sillä pakkauksessa tieto on muuttunut alkuperäisestä ja tätä tietoa ei ole talletettu mihinkään.

