#Viikko 33

Tutustuttu Lempel-Ziv algoritmi perheeseen mm. kirja Data compression The complete reference, 4th ed. ja netin eri lähteiden kautta.

Aluksi tuli tutkittua ja korjattua DES:n CBC ongelma joka paljastui virheeksi itse algoritmissa. Kyseessä oli virheellinen käsittely CBC:n purussa olevan edellisen blokin käsittelystä. Kts [Cipher Block Chaining (CBC)](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Block_Chaining_.28CBC.29) kuinka sen pitäisi toimia.

Perustoteutus [Lempel-Ziv-Welch](https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch) tuli valmiiksi viikonlopun aikana joskin korjailua tuli tehtyä myöhemminkin ja kokeiltua eri tietorakenteita operaatioden kanssa. Omien tietorakenteiden toteutus ja toimivuuden varmistus Javan sisäisten luokkien tilalle vei jonkin verran aikaa sillä samalla tuli kokeiltua muitakin mahdollisia vaihtoehtoja kuten [Trie](https://en.wikipedia.org/wiki/Trie). Osa alkuviikkoa ja loput viikosta kului testien parissa. Tarkoitus oli saada testaus melko kattavaksi ja aika hyvin tämä onnistui. Myös DES:n testaus Javan tarjoamaan toteusta varten onnistui hetken kestäneen tiedon kaivelun jälkeen. Nyt voi todeta että omatekoinen DES:n toimii aivan kuten Javan tarjoama tiettyjen valintojen jälkeen. Valinnoilla tarkoitan asioita kutenesimerkiksi padding toimintoa tai CBC:ä.

Opittua tuli että jotkin hyvältä vaikuttavat tietorakenteet eivät sovellu näppärästi kaikkiin tarvittaviin tilanteisiin ilman mahdollisesti muita tietorakenteita ja näiden omien tietorakenteiden hyvä toteutus vie aikaa. Mielenkiintoisena asiana tuli opittua Javan rajapintojen toteutuksesta, esim. [älä koskaan luo yhdellä luokalla Iterator ja Iterable rajapintoja](http://stackoverflow.com/questions/5836174/java-iterator-and-iterable/5836220#5836220), ja miksi [geneeristen taulujen](http://stackoverflow.com/questions/18581002/how-to-create-a-generic-array/18581313#18581313) luonti aiheuttaa ongelmia sekä kääntäjä herjoja.

Tällä hetkellä pakkaukselle ja purulle ei ole komentorivikäyttöliittymää tehty sillä aika meni itse toiminnalisuuden, omien tietorakenteiden ja testauksen kanssa.

Tarkoitus on saada em. komentorivikäyttöliittymä tehtyä sekä tutkia mm. toteutuksen toimintaa ja tilan käyttöä. Toiveena olisi mahdollisesti joko toteuttaa toinen salaus- tai pakkausalgoritmi tai tutkia nyt tehdyn pakkausalgoritmin parantamista.

###Testaus

Käytössä on Cobertura ja PIT. Testaus keskittyy omiin algoritmeihin ja tietorakenteisiin jotka sijaitsevat crypto, compression ja utility paketeissa. Kaikkien näiden rivikattavuus on tällä hetkellä 100%. Coberturen ilmoittama haarakattavuus on crypto 98%, compression 93% ja utility 95%. PIT:n mutaatiokattavuus on crypton osalta 95%, compression 96% ja utilityn 92%.
