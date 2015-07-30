# Viikko 31

Tiralabran aiheen valinta. Aloitettu tutustumisella valittuun aiheeseen ja etsimällä siitä toteutukseen sopivaa osaa. Aiheeseen tutustuttu esimerkiksi Wikipedian [tiedon pakkaukseen](https://en.wikipedia.org/wiki/Data_compression), [häviöttömiin (lossless)](https://en.wikipedia.org/wiki/List_of_algorithms#Lossless_compression_algorithms) & [häviöllisiin (lossy)](https://en.wikipedia.org/wiki/List_of_algorithms#Lossy_compression_algorithms) algoritmeihin, alan [kirjallisuuteen](http://www.helsinki.fi/kirjasto/fi/haku?q=data%20compression&type=monograph&lang=&online_only=false&use_year_range=false&start_year=1977&end_year=2015&sort=_score&curpage=1) mm. Applied Crytpography kirjoittanut Bruce Schneier ja Salausmenetelmät kirjoittanut Petteri Järvinen.

Ohjelmaan on luotu GitHub projekti sekä Netbeans Maven projekti.

Tähän mennessä opittua on että pakkausalgoritmeja on valtava määrä, esimerkiksi yksistään [Lempel-Ziv variaatiota](https://en.wikipedia.org/wiki/List_of_algorithms#Lossless_compression_algorithms) on tusinan verran, ja niiden toteutusten teko on joissain tapauksissa todella suuri ja vaativa toimenpide, esimerkiksi [bzip2](https://en.wikipedia.org/wiki/Bzip2#Compression_stack). Onneksi mukaan mahtuu myös helpommin toteutettavia kuten vaikka [Lempel-Ziv](https://en.wikipedia.org/wiki/LZ77_and_LZ78).

Salausalgoritmejakin on useita, mutta hyviä tuntuu olevan vähemmän. Jos salausta on tarkoitus käyttää kannattaa turvautua olemassa olevaan, hyvin tunnettuun ja testattuun algoritmiin kuin yrittää kehittää omaansa. Hyvänä taustana on esimerkiksi [Bruce Schneierin Why cryptography is harder than it looks](https://www.schneier.com/essays/archives/1997/01/why_cryptography_is.html) ja [Security pitfalls in cryptography](https://www.schneier.com/essays/archives/1998/01/security_pitfalls_in.html).

Seuraavana tavoitteena on lähteä luomaan yhtä salausalgoritmia joka alustavan ajatuksen mukaan olisi [DES](https://en.wikipedia.org/wiki/Data_Encryption_Standard). Tunnettu ja hyvin dokumentoitu algoritmi vaikkakin nykyisin sitä pidetään turvattomana johtuen sen helpotasta murrettavuudesta jopa ns. brute force menetelmällä; kts. [The DES cracker](http://w2.eff.org/Privacy/Crypto/Crypto_misc/DESCracker/HTML/19980716_eff_des_faq.html).

