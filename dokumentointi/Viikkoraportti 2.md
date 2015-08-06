# Viikko 32

Tutustuttu [DES (Data encryption standard)](https://en.wikipedia.org/wiki/Data_Encryption_Standard) algoritmiin mm. Applied Crytpography kirjan sekä netin eri lähteiden kautta.

Aloitettu toteutuksen teko luomalla crypto package ja sinne DES luokka. Perustoiminnallisuus ilman apufunktioita, mm. encrypt ja decrypt, sekä CBC toteutusta tuli tehtyä viikonlopun aikana. Alkuviikosta tuli luotua Apache Commons CLI:n avulla yksikertaista komentorivikäyttöliittymää sovellukselle. Viikon keskivaihe meni apufunktioiden ja testauksen parissa.

Tuli opittua että varsinainen DES:n ydin käsittelee vain kiinteää määrää tavuja joita se pyörittelee ja joiden bittejä se sotkee. Tämä tarkoittaa sitä että jos salaa oletuksella, eli ns. [Electronic Cookbook (ECB)](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Electronic_Codebook_.28ECB.29) tilalla kahden saman sisältöisen tavujonon salaus on identtinen keskenään. Jos katsoo [edellisen](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Electronic_Codebook_.28ECB.29) linkin takaa esitettävää kuvan salausta ymmärtää ehkä paremmin mistä on kyse. Tämän tilalla tulisi käyttää siis jotain muuta, kuten [Cipher Block Chaining (CBC)](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Block_Chaining_.28CBC.29), tilaa.

Tällä hetkellä tehty toteutus ei käytä CBC tilaa koska sen testaamisessa ja käytämisessä komentorivin kautta paljasti oudon eron joka ei heti selvinnyt. Ongelma lienee komentorivin suora osajonon salaus ja purku sillä testaamisessa käytetyt koko tavujonon salaus ja purku toimivat oletetusti.

Seuraavana on tarkoitus tutkia ongelma CBC:n kanssa ja korjata se. Sen jälkeen tarkoitus on tutustua Lempel-Ziv pakkaukseen ja lähteä toteuttamaan jotain sen variaatiota.

