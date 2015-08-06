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
