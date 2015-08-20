#Toteutus

##Käyttöohje

##Toiminta ja rakenne

Ohjelma jakaantuu neljään pakkaukseen; compression, crypto, utility ja cryptcomp.

### Pakkaus

 Compression sisältää toteutetun pakkausalgoritmin [Lempel-Ziv-Welch](https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch), joka on paranneltu Lempel-Ziv 78. Algoritmin ytimessä on pakkaus, compress, ja purku, decompress, funktiot. Molemmat luovat alussa perussanakirjan 256 ensimmäisestä ASCII merkistä. Tämän jälkeen ne käyvät läpi itse pakattavan tai purettavan tiedon joista rakentuu lopullinen sanakirja.
 Pakkaus sisältää myös toisen luokan, BitPacker, jota voidaan käyttää myös itsenäisenä osan tiivistämään sanaa, 16-bittiä, pienempiä arvoja yhteen sanaan pakkaamalla niitä bittitasolla. Esimerkiksi 10-bittisiä arvoja voitaisiin näin ollen tiivistää 8 kappaletta 5 sanaan, 8 * 10 bittiä = 80 bittiä = 5 * 16 bittiä.
 BitPacker voidaan myös yhdistää em. LZW pakkauksen pakatun muodon jatkopakkaukseen.

### Salaus

 Crypto sisältää toteutetun salausalgoritmin [DES (Data encryption standard)](https://en.wikipedia.org/wiki/Data_Encryption_Standard). Algoritmin ydin, des funktio, joka hoitaa varsinaisen tiedon pakkauksen tai salauksen.

### Työkalut

 Utility sisältää apuna käytettävät tietorakenteet; list, map ja trie. List ja map ovat yksinkertaistettuja rakenteita Javan vastaavasta. Niiden tarkoitus on tarjota toteutus ilman Javan tarjoamia rakenteita. Trie on puumainen rakenne joka tallentaa tiedon järjestetyssä muodossa ja samalla hieman pakkaa tallennettavaa tietoa.

