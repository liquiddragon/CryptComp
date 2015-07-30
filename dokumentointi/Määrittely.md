# Määrittely

Tarkoituksena on toteuttaa aluksi yksi salaus- ja pakkausalgoritmi sekä perehtyä niiden toimintaan ja tehokkaaseen toteutukseen niiltä osin kuin se on käytännöllistä. Olemassa olevat salausalgoritmit kerrotaan luokitelluksi pääsääntöisesti O(n) luokkaan. Kts. [Big-O Notation: Encryption Algorithms](http://crypto.stackexchange.com/questions/2338/big-o-notation-encryption-algorithms). Pakkausalgoritmit vaihtelevat jonkin verran, mutta esimerkiksi Lempel-Ziv-Welch (LZW) ja Huffman luokitellaan myös O(n). Kts. [Big O complexities of algorithms - LZW and Huffman](http://stackoverflow.com/questions/6189765/big-o-complexities-of-algorithms-lzw-and-huffman).

Toteutettavat algoritmit on tarkoitus kapseloida omiin pakkauksiinsa, Java kielen mukaan, siten että projektista voisi irroittaa halauamansa algoritmin toteuttavan pakkauksen, liittää sen omaan projektiinsa ja alkaa käyttää sitä ilman muutoksia lähdekoodiin.

Toteutusta on tarkoitus pystyä käyttämään kokeellisesti paikalliselta koneelta ainakin komentorivipohjaisella käyttöliittymällä.

Käyttäjinä ovat toteutettua salaus- tai pakkausalgoritmia tarvitsevat projektit joita ei kuitenkaan tehtävän luonti hetkellä ole tiedossa.


