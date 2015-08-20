#Viikko 34

Lempel-Ziv-Welch sisäisen toteutuksen muuttaminen käyttämään omatekoista [trie](https://en.wikipedia.org/wiki/Trie) rakennetta sanakirjassa. Vaikuttaa tehokkaammalta ja vähemmän muistia kuluttavalta kuin alkuperäinen map ratkaisu, mutta vaatii vielä testausta. Toteutettu trie on hieman muokattu versio sanakirjan käyttöön sillä perinteinen trie toimii hyvin pakkauksen kanssa, muttei ollenkaan purussa missä pitäisi tietää viimeisen alkion arvo. Tätä varten toteutettu trie käyttää sisäisesti map kokoelmaa jonne tallennetaan ketjun viimeisen alkion arvo sekä linkki siihen alkioon. Lisäksi sisäinen node tallentaa tiedon omasta vanhemmastaan sekä tämän arvon että takaperoinen haku onnistuu. Näin tämä tallennusratkaisua voidaan käyttää myös purun yhteydessä tarvittavan sanakirjan kohdalla.

Lisäksi em. LZW tuli muutettua siten että se ottaa pakkauksessa ja tuottaa purussa tavunjonon, eli aiemmin käytössä ollut `String` on poistettu käytöstä.

Mielenkiintoisena asiana tuli selväksi että suoraa bittien pakkausta sanaan, 16-bittiä, ei tuntunut olevan tarjolla missään yleiskäyttöisenä kirjastona. Muutama harva tämän suuntainen toteutus johon törmäsin oli aina integroituna johonkin olemassa olevaan toteutukseen jota ei pysytynyt mitenkään järkevästi irroittamaan käytetystä ympäristöstään. Näin ollen päädyin toteuttamaan oman luokan toiminnasta. Toteutus vaati hieman mietintää sekä testaamista eri arvoilla sillä esimerkiksi pariton bittien lukumäärän toiminnan sain nopeammin kuntoon kun parillisuuden tuottama sanajaon osuus vaati pientä lisäpohdintaa. Debuggeri ja sopivat muut apuvälineet ovat hyödyksi tämänkaltaisen asian kanssa sekä tietysti pitkä pinna.

`BitPacker` luokka on tehty sitä varten että voisin tutkia kuinka sen toteuttama toiminnallisuus muuttaa LZW algoritmin lopputulosta pakkauksen yhteydessä. Koska halusin pitää tämän erillään varsinaisesta LZW toteutuksesta se on toteutettu omana luokkanaan. Näin sen voi ottaa käyttöön tarpeen mukaan tai jättää pois. Luokka selvittää sille kerrotun tiedon, suurin syötteessä esiintyvä luku, perusteella esitykseen tarvittavien bittien lukumäärän ja käyttää tätä tietoa pakatessaan syötteen uuteen taulukkoon, jossa alkiona on sana, eli 16-bittiä. Algoritmi on yritetty tehdä melko selkeäksi myös muuttujien nimeämisen kohdalta että myös muut ymmärtäisivät sitä paremmin, joskin bitit ja niiden käsittely perustasolla voi olla hyvä tuntea entuudestaan. Esimerkiksi [Bitwise operation](https://en.wikipedia.org/wiki/Bitwise_operation).

Dokumentaatiota on päivitetty kokoelmien lisäyksen takia ja pakkauksen osalta vastaamaan muuttuneita tietoja.

Projektista puuttuu edelleen pakkauksen käyttö komentoriviltä. Tämä on tarkoitus toteuttaa seuraavaksi ja sitä hyödyntäen tutkia miten LZW toimii erilaisten tiedostojen kohdalla sekä miten BitPacker vaikuttaa tulokseen.

###Testaus

Muutosta viime viikkoiseen ei suuremmin ole. Pientä tason nousua joissain kohdin on, mutta muuten lähes samalla tasolla. Lisää testausta on toki tullut uusien toiminnallisuuksien ja luokkien johdosta. Kaikkien testattavien osien rivikattavuus on tällä hetkellä 100%. Coberturen ilmoittama haarakattavuus on crypto 98%, compression 93% ja utility 96%. PIT:n mutaatiokattavuus on crypton osalta 95%, compression 100% ja utilityn 92%.
