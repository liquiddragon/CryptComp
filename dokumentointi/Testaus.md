# Testaus

## Pakkaus

Pakkauksen, sekä Lempel-Ziv-Welch että BitPacker, perustestaus on tehty JUnit yksikkötesteillä. Testit ovat itse kehitettyjä. Ne testaavat perustoiminnallisuutta ja mahdollisesti muutamia erikoistapauksia tarpeen mukaan.

Suurin osa LZW:n testausta on tehty komentorivikäyttöliittymää käyttäen erilaisilla tiedostoilla. Tämä johtuu siitä että erilaisia raja-arvoihin liittyviä tapauksia ei ollut mielekästä testata yksikkkötestien kautta, koska näiden testiaineistojen koko täytyi olla tarpeeksi suuri että testaus tuli tehtyä. Esimerkiksi LZW:n 14-bittinen sanakirja kelpuuttaa 16384 merkintää sanakirjaan ennen kuin se täytyy. Näin ollen syötteen täytyisi olla huomattavan suuri.

BitPacker:n kohdalla yksikkötestit testaavat eri bittimäärien toimintaa pakkauksessa ja purussa verraten purun tulosta alkuperäiseen aineistoon. Tämä on melko suoraviivainen tapa. Lisänä on käytetty komentorivikäyttöliittymän kautta tapahtuvaa testausta.

Testiaineistona on ollut pääsääntöisesti tekstitiedostoja johtuen Javan tietotyyppien tuomasta ongelmasta. Katso tarkemmin kohdasta Huomioitavaa [Toteutus](https://github.com/liquiddragon/CryptComp/tree/master/dokumentointi/Toteutus.md) dokumentista.

Esimerkki aineistona ovat olleet muun muassa: [Cosmos](http://textfiles.com/rpg/cosmos) n. 400 kB, CCTrie luokan lähdekoodi n. 1.7 kB ja lyhyt teksitiedosto n. 21 tavua. Lisäksi pakkausta on kokeiltu myös OpenDocument tiedostoon, n. 222 kB, ja bittikarttakuvaan, n. 6.9 MB.

### Tuloksia

**Cosmos**

|Pakkaus *)|Koko|
|-------|----|
|Alkuperäinen|393,3 kB|
|9|284,8 kB|
|9 nb|506,3 kB|
|10|259,9 kB|
|10 nb|415,9 kB|
|11|225,0 kB|
|11 nb|327,3 kB|
|12|209,2 kB|
|12 nb|278,9 kB|
|13|190,3 kB|
|13 nb|234,2 kB|
|14|179,3 kB|
|14 nb|205,0 kB|
|15|165,7 kB|
|15 nb|176,8 kB|
|16|321,6 kB|
|16 nb|321,6 kB|

*) Pakkaus sarakkeessa esiintyvä numero kertoo käytetyn sanakirjan enimmäiskoon bitteinä. Merkintä `nb` tarkoittaa että BitPacker:a ei ole käytetty.


**CCTrie lähdekoodi**

|Pakkaus *)|Koko|
|-------|----|
|Alkuperäinen|1714 B|
|9|891 B|
|9 nb|1577 B|
|10|785 B|
|10 nb|1251 B|

*Huom*: Sanakirjaan koon kasvattaminen 10-bitistä ylöspäin ei vaikuttanut tuloksiin ollenkaan.

*) Pakkaus sarakkeessa esiintyvä numero kertoo käytetyn sanakirjan enimmäiskoon bitteinä. Merkintä `nb` tarkoittaa että BitPacker:a ei ole käytetty.


**Lyhyt tekstitiedosto**

|Pakkaus *)|Koko|
|-------|----|
|Alkuperäinen|21 B|
|9|31 B|
|9 nb|49 B|

*Huom*: Sanakirjaan koon kasvattaminen 9-bitistä ylöspäin ei vaikuttanut tuloksiin ollenkaan. Koon kasvaminen bittipakatussa tapauksessa selittyy otsikkotietueesta jota käytetään apuna tiedoston purkamisessa. Ilman bittipakkausta koon kasvu selittyy otsikkotietueella ja sillä että jokainen tavu, 8-bittiä, tallennetaan yhteen sanaan, 16-bittiä.

*) Pakkaus sarakkeessa esiintyvä numero kertoo käytetyn sanakirjan enimmäiskoon bitteinä. Merkintä `nb` tarkoittaa että BitPacker:a ei ole käytetty.


**OpenDocument tekstitiedosto**

|Pakkaus *)|Koko|
|-------|----|
|Alkuperäinen|221,9 kB|
|9|247,8 kB|
|10|273,2 kB|
|11|296,0 kB|
|12|313,6 kB|
|13|322,8 kB|
|14|320,1 kB|
|15|307,2 kB|
|16|584,1 kB|

*Huom*: Oudolta vaikuttava testitulos pakkauksen koon kasvamisesta alkuperäiseen nähden selittyy sillä että OpenDocument, vaikkakin XML-pohjainen, tallennetaan oletuksena JAR, Java Archive, muodossa, joka taas käyttää ZIP-pakkausta. Katso esimerkiksi Wikipedian artikkelia [OpenDocument technical specification](https://en.wikipedia.org/wiki/OpenDocument_technical_specification).

*) Pakkaus sarakkeessa esiintyvä numero kertoo käytetyn sanakirjan enimmäiskoon bitteinä.


**Testikuva, pakkaamaton bittikartta**

|Pakkaus *)|Koko|
|-------|----|
|Alkuperäinen|6,9 MB|
|9|5,9 MB|
|10|4,5 MB|
|11|3,8 MB|
|12|3,7 MB|
|13|3,7 MB|
|14|3,5 MB|
|15|3,3 MB|
|16|6,2 MB|

*) Pakkaus sarakkeessa esiintyvä numero kertoo käytetyn sanakirjan enimmäiskoon bitteinä.

## Salaus

DES:n testaus on tehty JUnit yksikkötesteillä joissa käytetään omien testien lisäksi myös Javan sisäänrakennetun DES:n toiminnallisuutta varmentamaan omatekoisen toteutuksen toimivuus kolmannen osapuolten toteutusta vasten. Näiden lisäksi on tehty myös lisätestejä komentorivikäyttöliittymän kautta.

### Tuloksia

|Tiedosto|Aika|Huomioita|
|--------|----|---------|
|Lyhyt teksti, 1.7 kB|kryptaus ja purku: ~2 ms| - |
|Cosmos|kryptaus: ~64 ms, purku: ~83 ms| Heittely suurta joten suurimmat heitot poistettu. Syy on todennäköisimmin JVM:n sisäinen toiminta.|
|test.bmp|kryptaus: ~524 ms, purku: ~587 ms| Vastaavaa heittelyä kuin Cosmoksen kanssa.|

## Kokoelmat

Kokoelmien testaus on tehty JUnit yksikkötesteillä. Testit ovat itse kehitettyjä. Pääpaino on ollut testata niitä toiminnallisuuden osia jotka eivät tulleet testatuksi muiden luokkien puolesta jotka käyttävät näitä kokoelmia.


