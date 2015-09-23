# Käyttöohje

## Projektin yleinen käyttö

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
|-timing|pääoperaation ajankulutus|

*Huomioitavaa:*
- Älä yhdistä salaukseen (`dec`, `enc` & `key`) ja pakkaukseen (`pack`, `unpack`, `nobit` ja `dictsize`) liittyviä valitsimia.
- `infile`, `outfile` ja `timing` toimivat molempien, salauksen ja pakkauksen, kanssa.
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


## Luokkien käyttö ohjelmoijan kannalta

Luokkien [Javadoc](https://github.com/liquiddragon/CryptComp/tree/master/dokumentointi/javadoc) selviää pääasiassa kuinka niitä käytetään.

### DES

#### Salaus

1. Luo luokka ja aseta käytettävä avain joko merkkijonona tai tavuina. Esim. `DES des = new DES("salasana")`
2. Valinnainen: vaihda oletuksena käytetty [CBC](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Block_Chaining_.28CBC.29):n [ECB](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Electronic_Codebook_.28ECB.29):n. `des.setOperationMode(DES.OperationMode.ECB)`
3. Suorita salaus. `byte[] output = des.encrypt(input)`

#### Salauksen purku

1. Luo luokka ja aseta käytettävä avain joko merkkijonona tai tavuina. `DES des = new DES("salasana")`
2. Valinnainen: vaihda oletuksena käytetty [CBC](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Block_Chaining_.28CBC.29):n [ECB](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Electronic_Codebook_.28ECB.29):n. `des.setOperationMode(DES.OperationMode.ECB)`
3. Suorita salauksen purku. `byte[] output = des.decrypt(input)`

### Pakkaus

#### LZW pakkaus

1. Luo luokka. `LempelZivWelch lz = new LempelZivWelch()`
2. Suorita pakkaus. `CCList<Integer> compressed = lz.compress(input)`

#### LZW purku

1. Luo luokka. `LempelZivWelch lz = new LempelZivWelch()`
2. Suorita purku. `int[] output = lz.decompress(input)`

### BitPacking pakkaus

1. Luo luokka. `BitPacker bp = new BitPacker()`
2. Aseta suurin arvo syötteessä. `bp.maxValue(1000)`
3. Suorita pakkaus. `int[] output = bp.pack(input)`

### BitPacking purku

1. Luo luokka. `BitPacker bp = new BitPacker()`
2. Aseta suurin arvo syötteessä. `bp.maxValue(1000)`
3. Suorita purku. `int[] output = bp.unpack(input, packed_input_length)` Huomaa: Packed input length sisältää pakattujen merkkien lukumäärän jonka saa käyttöönsä pakkauksen jälkeen komennolla `bp.packedCount()`.

### Muut luokat

Kokoelmat ovat melko suoraviivaisia käyttää joten niiden käyttö pitäisi selvitä [Javadoc](https://github.com/liquiddragon/CryptComp/edit/master/dokumentointi/javadoc):sta.
