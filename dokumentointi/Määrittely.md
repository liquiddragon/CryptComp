# Määrittely

Tarkoituksena on toteuttaa aluksi yksi salaus- ja pakkausalgoritmi sekä perehtyä niiden toimintaan ja tehokkaaseen toteutukseen niiltä osin kuin se on käytännöllistä. Olemassa olevat salausalgoritmit kerrotaan luokitelluksi pääsääntöisesti O(n) luokkaan. Kts. [Big-O Notation: Encryption Algorithms](http://crypto.stackexchange.com/questions/2338/big-o-notation-encryption-algorithms). Pakkausalgoritmit vaihtelevat jonkin verran, mutta esimerkiksi Lempel-Ziv-Welch (LZW) ja Huffman luokitellaan myös O(n). Kts. [Big O complexities of algorithms - LZW and Huffman](http://stackoverflow.com/questions/6189765/big-o-complexities-of-algorithms-lzw-and-huffman).

Toteutettavat algoritmit on tarkoitus kapseloida omiin pakkauksiinsa, Java kielen mukaan, siten että projektista voisi irroittaa halauamansa algoritmin toteuttavan pakkauksen, liittää sen omaan projektiinsa ja alkaa käyttää sitä ilman muutoksia lähdekoodiin.

Toteutusta on tarkoitus pystyä käyttämään kokeellisesti paikalliselta koneelta ainakin komentorivipohjaisella käyttöliittymällä.

Käyttäjinä ovat toteutettua salaus- tai pakkausalgoritmia tarvitsevat projektit joita ei kuitenkaan tehtävän luonti hetkellä ole tiedossa.

## Salaus

Ensimmäinen toteutus on [DES (Data encryption standard)](https://en.wikipedia.org/wiki/Data_Encryption_Standard). Algoritmin ydin, des funktio,
tekee joukon vakioaikaisia operaatiota alussa ja lopussa sekä välissä kiinteän lukumäärän kierroksia kuten standardi määrittää. Tämä funktio käsittelee vain kahdeksan tavua kerrallaan, ei siis koko viestiä. Sekä encryptBlock että decryptBlock käyttävät tätä funktiota toimintansa ytimessä. Apufunktio, encrypt ja decrypt, salaavat tai purkavat annetun tavujonon kokonaisuudessaan.

Apufunktioiden, encrypt ja decrypt, aikavaatimus on siis viestin pituus, m, eli O(m). Varsinainen ydinkfunktio, des, voidaan ajatella toimivan vakioajassa eli O(1).

Apufunktiot, encrypt ja decrypt, käyttävät annetun tavujonon, m, sekä apuna käytettävien samankokoisen tavujonojen verran tilaa eli O(3m).

Muut apufunktiot käyttävät pienempiä osuuksia aikaa tai tilaa. Esimerkiksi padding ja byte-int-byte muunnokset käsittelevät vain kahdeksaa tavua kerrallaan tehden vakioaikaisia toimintoja. Avaimen luonti merkkijonosta käyttää avaimen pituuden, ap, verran aikaa eli O(ap) ja tilaa vain kiinteän mittaisen avaimen verran eli kahdeksan tavua. DES:n osa-avainten luonnissa käydään kiinteä, standardin mukainen kierroksmäärä läpi jonka aikavaatimus on siis kierrosten lukumäärä kertaa kiinteä avaimen pituus. Muistinkulutus on kiinteä avaimen sekä apurakenteiden pituus ja jossa apurakenteiden pituudet ovat vakiot.

## Pakkaus

 Ensimmäinen toteutus on [Lempel-Ziv-Welch](https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch) eli paranneltu Lempel-Ziv 78. Algoritmin ytimessä on pakkaus, compress, ja purku, decompress, funktiot. Molemmat luovat alussa perussanakirjan 256 ensimmäisestä ASCII merkistä. Tämän jälkeen ne käyvät läpi itse pakattavan tai purettavan tiedon.
 Pakkauksessa käydään kaikki tiedon merkit, m, läpi eli aika vaativuus on O(m). Haettavaa alkiota etsitään trie kokoelmasta tieto kerrallaan joten keskimäärin hakuun menee O(sanakirjan koko/2) aikaa. Mikäli alkiota ei löydy se lisätään sanakirjaan johon kuluu O(sanakirjan koko) aikaa sillä lisäys tarkistaa ensin löytyikö alkiota jo sanakirjasta ja jos löytyy korvaa vanhan avaimen arvon uudella. Normaalimpi tapaus on että sitä ei löydy jolloin se lisätään sanakirjan loppuun. Sanakirjan koko kasvaa alkion kerrallaan.
 Muistia pakkauksessa sanakirjan osalta kuluu O(256 + lisättävien alkioiden lkm) muistia. Itse pakattavan tiedon tarven vaihtelee riippuen pakkauksen onnistumisesta, mutta huonossa tapauksessa pakattavan tiedon koko voi jopa kasvaa alkuperäisestä.
 Purun aika- ja muistivaatimukset ovat lähes identtiset sillä alku on sama. Purussa käydään pakattu tieto, n, läpi eli aikaa kuluu O(n). Jokaista pakatun tiedon osaa etsitään sanakirjasta johon kuluu keskimäärin O(sanakirjan koko/2) aikaa ja mikäli sitä ei sieltä löydy luodaan uusi sanakirjamerkintä kuten pakkauksessa.

 BitPacker on aputoiminto LZW:lle, mutta siitä täysin erillinen eli sitä voi käyttää myös itsenäisenä toimintonaan. BitPacker:n aikavaativuus pakkauksessa ja purusssa on keskimäärin noin O(syötteen koko) ja tilavaativuus O(syöte * 2). Muut toiminnot ovat vakioaikaisia tai -tilaisia.

## Kokoelmat

Map: Tilanvaraus on alussa 16 alkiota ja tilan loppuessa sen määrä kaksinkertaistuu. Kasvatus vie aikaa O(alkioiden lkm) ja muistia O(3 * alkioiden lkm). Kolminkertainen tarkoittaa alkuperäistä alkioiden lukumäärää, n, plus uutta alkioiden lukumäärää, 2*n. Alkion lisäys käy läpi olemassa olevat alkiot tarkistaakseen onko kyseessä olemassa olevan avaimen arvon muutos. Tämä vie keskimäärin O(alkioiden lkm/2) mikäli avain on olemassa. Mikäli kyseessä on uusi avain aikavaatimus on O(alkioiden lkm) plus mahdollinen kapasiteetin kasvatus ja vakioaikainen lisäys. Alkion poisto vie keskimäärin O(alkioiden lkm/2) aikaa sekä tiivistyksen joka on keskimäärin sama kuin poistokohdan löytyminen. Avaimen etsintä vie keskimäärin O(alkioiden lkm/2) aikaa. Muistia kuluu alussa 16 alkioita ja myöhemmin tämän monin kertoja kokoelman kasvaessa. Poistossa muistia ei vapauteta.

List: Tilanvaraus on alussa 10 alkiota ja tilan loppuessa sen määrä kaksinkertaistuu. Kasvatus vie aikaa O(alkioiden lkm) ja muistia O(3 * alkioiden lkm). Kolminkertainen tarkoittaa alkuperäistä alkioiden lukumäärää, n, plus uutta alkioiden lukumäärää, 2*n. Alkion lisäys on vakioaikainen operaatio p.l. mikäli tila ei riitä. Tällöin tehdään tilan kasvatus. Alkion haku vie keskimäärin O(alkioiden lkm/2) aikaa. Poisto itsessään on vakioaikainen, mutta sen yhteydessä tehtävä tiivistys on keskimäärin O(alkioiden lkm/2).

Muut operaatiot edellisissä ovat vakioaikaisia ja -tilaisia. Esimerkiksi koon selvittäminen palauttaa vain sisäisen muuttujan arvon tai listan iteraattorin seuraavan elementin tarkistus tutkii onko k.o. indeksillä olemassa elementtiä.

Trie: Sisäinen talletus käyttää node luokasta koostuvaa puumaista rakennetta, jonka koko on melko pieni p.l. viitetaulukkoa joka alustetaan oletussanakirjan kooksi eli 256 elementtiä. Myös takaperoisen haun mahdollistava taulu vie muisti trie:hen talletettujen kokonaisten alkioiden plus yhden viitealkion verran tilaa. Lisääminen vie O(lisättävän alkion mitta) aikaa ja mustia sisäinen noden luokan verran, O(sis. node). Haut, eli get ja getReverse, vievät keskimäärin O(entry / 2) aikaa ilman erillisen lisätilan vaativuutta. Myös contains toimii samoilla vaatimuksilla kuin get. Muut metodit ovat vakioaikaisia ja -tilaisia.
