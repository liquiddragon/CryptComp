# Toteutus

## Yleiskuvaus
Ohjelma toteuttaa standardin mukaisen [DES (Data encryption standard)](https://en.wikipedia.org/wiki/Data_Encryption_Standard) algoritmin ja muunnelman [Lempel-Ziv-Welch (LZW)](https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch) pakkausalgoritmista. Lisäki toteutettuna on läheisesti LZW:n liittyvä bittitason pakkausalgoritmi.

Minkä tahansa algoritmin voi ottaa käyttöön omaan projektiinsa kunhan ottaa mukaan mahdollisesti tarvittavat tietorakenteet tai korvaa ne Javan tarjoamilla vastaavilla rakenteilla.

Luokka `DES.java` sisältää kaiken tarpeellisen eikä ole riippuvainen muista kuin normaaleista Javan palveluista. Luokka `LempelZivWelch.java` käyttää hyväkseen projektiin tehtyjä tietorakenteita; trie ja list. Ensimmäinen käyttää sisäisesti vielä omatekoista map kokoelmaa. Nämä voidaan ottaa mukaan projektista tai korvata Javan palveluilla. Jälkimmäisessä tapauksessa trie:n voisi korvata esimerkiksi Javan [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html) kokoelmalla ja list korvautuisi Javan [ArrayList](http://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html) kokoelmalla. `BitPacker.java` luokka on samanlainen kuin aiemmin mainittu `DES.java` eli sen voi ottaa käyttöön sellaisenaan.

Ohjelma käyttää hyväksee joitain Javan versio kahdeksassa tulleita ominaisuuksia joten tämä täytyy ottaa huomioon luokkien käytössä tai tämän projektin kääntämisessä. Käytössä on ollut OpenJDK versio 1.8.0_45-internal. Ohjeet tämän OpenJDK:n version käytöstä Ubuntu 14.04 LTS kanssa löytyy esimerkiksi aiemmasta [MiPuz ohjelman kääntäminen](https://github.com/liquiddragon/MiPuz/blob/master/dokumentointi/ohjelmanK%C3%A4%C3%A4nt%C3%A4minen.md) dokumentaatiosta.

Projektille on tehty myös yksinkertainen komentorivikäyttöliittymä jolla voi kokeilla salausta ja pakkausta. Pakkaukseen voi myös vaikuttaa muutamalla valinnalla. Komentorivikäyttöliittymä on lähinnä testausta varten eikä se välttämättä sovellu yleisempään käyttöön.

## Käyttöohje

Projektia voi käyttää Maven:n komentorivin kautta mm. seuraavasti:
- Kääntäminen: `mvn clean compile`
- Testien ajaminen: `mvn test`
- Cobertura (testikattavuus): `mvn cobertura:cobertura`
- Pit (mutaatiotestaus ja kattavuus): `mvn org.pitest:pitest-maven:mutationCoverage`

Normaalin käytännön mukaan useampia komentoja, edellä esimerkiksi `compile` ja `test`, voi käyttää yhdellä kertaa.

Normaaliin ohjelman käyttöön liittyviä komentorivivalitsimia ja niiden lyhyet kuvaukset:
|Valitsin|Selitys|
|--------|-------|
|-dec <DES>|salaamiseen käytettävä algoritmi; vain DES on toteutettu|
|-enc <DES>|salauksen purkamiseen käytettävä algoritmi; vain DES on toteutettu|
|-key <key>|salaus- tai purkuavain|
| | |
|-pack|syötteen pakkaus; LZW|
|-unpack|syötteen purku; LZW|
|-nobit|bittipakkaus jätetään tekemättä|
|-dictsize <SIZE>|pakotettu sanakirjan koko bitteinä pakkauksessa ja purussa; arvoväli 9-16|
| | |
|-infile <name>|salattava, pakattava tai purettava tiedosto lukua varten|
|-outfile <name>|tiedosto minne tulos talletetaan|

*Huomioitavaa:*
- Älä yhdistä salaukseen (`dec`, `enc` & `key`) ja pakkaukseen (`pack`, `unpack`, `nobit` ja `dictsize`) liittyviä valitsimia.
- `infile` ja `outfile` toimivat molempien, salauksen ja pakkauksen, kanssa.
- Ole huolellinen käyttämiesi valitsinten ja niiden arvojen kanssa. Esimerkiksi `dictsize` täytyy olla sama pakkaamisessa ja purussa mikäli haluaa puretun tiedoston täsmäävän alkuperäisen kanssa.
- Virheentarkistukset ovat minimissään joten tarkista käyttämäsi valitsimet, tiedostoihin mahdollisesti liittyvät polut & oikeudet ja lue mahdolliset virheilmoitukset huolellisesti.
- Pidä mielessä että komentorivikäyttöliittymä ei ole tämän projektin pääasiallinen tarkoitus joten sen toimivuudesta virheettömästi ei ole mitään takeita.

Esimerkki komentoja:
* `mvn clean compile test org.pitest:pitest-maven:mutationCoverage cobertura:cobertura` - käännä projekti puhtaalta pöydältä, suorita testit sekä Pit ja Cobertura.
* `mvn exec:java -Dexec.mainClass=cryptcomp.Main -Dexec.killAfter=-1 -Dexec.args="-enc DES -key password -infile input.txt -outfile input_crypted.txt"` - suorita salaus käyttäen salasanaa `password` lukien tiedon `input.txt` tiedostosta ja kirjoittaen salatun tiedon `input_crypted.txt` tiedostoon.
* `mvn exec:java -Dexec.mainClass=cryptcomp.Main -Dexec.killAfter=-1 -Dexec.args="-dec DES -key password -infile input_crypted.txt -outfile input_clear.txt"` - suorita salauksen purku käyttäen salasanaa `password` lukien tiedon `input_crypted.txt` tiedostosta ja kirjoittaen puretun selväkielisen tiedon `input_clear.txt` tiedostoon.
* `mvn exec:java -Dexec.mainClass=cryptcomp.Main -Dexec.killAfter=-1 -Dexec.args="-pack -dictsize 10 -infile source.txt -outfile source_packed.txt"` - pakkaa tiedosto `source.txt` käyttäen 10-bitin sanakirjaa sekä bittipakkausta ja kirjoita pakattu tieto `source_packed.txt` tiedostoon.
* `mvn exec:java -Dexec.mainClass=cryptcomp.Main -Dexec.killAfter=-1 -Dexec.args="-unpack -dictsize 10 -infile source_packed.txt -outfile source_unpacked.txt"` - pura tiedosto `source_packed.txt` käyttäen 10-bitin sanakirjaa sekä bittipakkausta ja kirjoita purettu tieto `source_unpacked.txt` tiedostoon.
* `mvn exec:java -Dexec.mainClass=cryptcomp.Main -Dexec.killAfter=-1 -Dexec.args="-pack -nobit -infile source.txt -outfile source_packed.txt"` - pakkaa tiedosto `source.txt` käyttäen oletuksena 12-bitin sanakirjaa, mutta ilman bittipakkausta (`-nobit`) ja kirjoita pakattu tieto `source_packed.txt` tiedostoon.

## Toiminta ja rakenne

Ohjelma jakaantuu neljään pakkaukseen; compression, crypto, utility ja cryptcomp. Pakkaamiseen liittyvä toiminnallisuus on compression pakkauksessa. Salauksen toiminnallisuus on crypto pakkauksessa. Utility pakkaus sisältää omien tietorakenteiden toteutukset. Komentorivikäyttöliittymä apuluokkineen on cryptcomp pakkauksessa.

### Luokkarakenne

Projektin pääluokkakaavio löytyy [ohesta](https://github.com/liquiddragon/CryptComp/tree/master/dokumentointi/CryptComp whole diagram.pdf) ja päätoteutuksen osalta hieman tarkempi [kaavio](https://github.com/liquiddragon/CryptComp/tree/master/dokumentointi/CryptComp class diagram.pdf).

### Pakkaus

 Compression sisältää toteutetun pakkausalgoritmin [Lempel-Ziv-Welch](https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch), joka on paranneltu Lempel-Ziv 78. Algoritmin ytimessä on pakkaus, compress, ja purku, decompress, funktiot. Molemmat luovat alussa perussanakirjan 256 ensimmäisestä ASCII merkistä. Tämän jälkeen ne käyvät läpi itse pakattavan tai purettavan tiedon joista rakentuu lopullinen sanakirja.
 Lempel-Ziv-Welch luokka käyttää sisäisenä tietorakenteenaan CCList ja CCTrie luokkia. Jälkimmäinen käyttää vielä apunaan CCMap luokkaa.

 Pakkaus sisältää myös toisen luokan, BitPacker, jota voidaan käyttää myös itsenäisenä osan tiivistämään sanaa, 16-bittiä, pienempiä arvoja yhteen sanaan pakkaamalla niitä bittitasolla. Esimerkiksi 10-bittisiä arvoja voitaisiin näin ollen tiivistää 8 kappaletta 5 sanaan, 8 * 10 bittiä = 80 bittiä = 5 * 16 bittiä.
 BitPacker voidaan myös yhdistää em. LZW pakkauksen pakatun muodon jatkopakkaukseen.

### Salaus

 Crypto sisältää toteutetun salausalgoritmin [DES (Data encryption standard)](https://en.wikipedia.org/wiki/Data_Encryption_Standard). Algoritmin ydin, des funktio, joka hoitaa varsinaisen tiedon pakkauksen tai salauksen.

### Työkalut

 Utility sisältää apuna käytettävät tietorakenteet; list, map ja trie. List ja map ovat yksinkertaistettuja rakenteita Javan vastaavasta. Niiden tarkoitus on tarjota toteutus ilman Javan tarjoamia rakenteita. Trie on puumainen rakenne joka tallentaa tiedon järjestetyssä muodossa ja samalla hieman pakkaa tallennettavaa tietoa.

## Huomioitavaa

Javan tietotyypit ovat aina etumerkillisiä; [Primitive Data Types](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html). Tämä aiheuttaa ongelman ohjelman käytössä pakkauksen kohdalla sillä pakkauksessa käytetään sisäisesti pakattavan tavun arvoa indeksinä. Näin ollen tiedostossa olevat tavut joiden arvo ylittää 127 tulkitaan negatiivisiksi arvoiksi Javan toimesta. Komentorivikäyttöliittymään on tehty toiminto joka muuttaa nämä arvot etumerkittömiksi kokonaisluvuiksi [Byte.toUnsignedInt](https://docs.oracle.com/javase/8/docs/api/java/lang/Byte.html#toUnsignedInt-byte-) komennolla. Tämä mahdollistaa pakkauksen testaamisen myös binääritiedostoilla toteutuksen tehokkuuden tutkimiseksi muiden kuin tekstitiedostojen kohdalla. Haittana on että näin pakattuja tiedostoja ei voi purkaa sillä pakkauksessa tieto on muuttunut alkuperäisestä ja tätä tietoa ei ole talletettu mihinkään.
