ORFinder 1.0
Datum: 29-03-2019
Auteurs:
	Cas van Rijbroek
	Elco van Rijswijk
	Lex Bosch
	
Vragen/problemen kunt u sturen naar CFM.vanRijbroek@student.han.nl

De ORFinder is een applicatie ontworpen voor biologen voor het vinden van open reading
frames in DNA sequenties. De applicatie bied tevens de mogelijkheid om de gevonen reading
frames aan een functie te koppelen via BLAST.

Gevonden resultaten kunnen opgeslagen worden in een database. Het is ook mogelijk om
resultaten op uw lokale systeem op te slaan.

TUTORIAL:
Om de applicatie op te starten dubbelklikt u op 'ORFinder.jar'. U heeft Java 10+ nodig om
deze applicatie te kunnen draaien. 

OPMERKINGEN: 
	- De ingevoerde header kan (in tegenstelling tot de sequentie) altijd handmatig
	  aangepast worden. Hiermee kunt u resultaten handmatig onderscheiden van elkaar.
	- Bij 'status' zal tijdens het gebruiken van de applicatie steeds aangeven of uw
	  laatste actie een succes was. Tevens zullen ernstige foutmeldingen getoond worden
	  via een pop-up.

U heeft 3 mogelijkheden:
1. Een FASTA bestand met een enkele DNA sequentie erin inladen (extensie .fasta of .fa)
	Om dit te doen hoeft u enkel het pad naar het bestand in te voeren naast 'Bestandpad:'
	(dit kunt u ook doen door een bestand te selecteren door op 'Bladeren' te klikken)
	Vervolgens drukt u op 'Bestand inladen' en zult u de header en sequentie uit het
	bestand gevisualiseerd zien in de applicatie.
2. Een eerder opgeslagen resultaat uit de database ophalen.
	Om dit te doen moet u de header van het eerder opgeslagen resultaat EXACT (hoofdletter
	en spatie gevoelig) invoeren bij 'Header' en vervolgens bij Data opties 'Haal
	resultaat op' aangeklikt hebben en op 'Voer uit in database' drukken. Hierna zult u de
	gegevens gevisualiseerd zien of bij 'Status' een melding zien over waarom het niet
	gelukt is om een resultaat in te laden.
3. Een eerder opgeslagen resultaat lokaal ophalen.
	Om dit toe doen moet u een map 'SavedResults' op dezelfde locatie als het .jar bestand
	dat u uitvoert om de applicatie op te starten (bij het opslaan van resultaten wordt
	deze map automatisch voor u aangemaakt).
	U moet de header van het opgeslagen resultaat EXACT(hoofdletter en spatie gevoelig)
	invoeren bij 'Header' en vervolgens bij Data opties 'Haal resultaat op' aangeklikt
	hebben en op 'Voer lokaal uit' drukken. Hierna zult u de gegevens gevisualiseerd zien
	of of bij 'Status' een medling zien over waarom het niet is gelukt om een resultaat
	in te laden.

Voor het zoeken naar open reading frames hoeft u enkel op 'Zoek ORF's' te drukken nadat
u een sequentie heeft ingeladen. De ORF's zouden snel gevisualiseerd moeten worden bij
'Resultaten' in de vorm van een dropdown menu. Voor ieder open reading frame zal de
start en stop positie op de ingevoerde sequentie worden weergeven.

Voor het BLASTen van open reading frames hoeft u enkel op de knop 'BLAST' te drukken.
WAARSCHUWING: de applicatie zal niet meer gebruikt kunnen worden totdat alle BLAST
resultaten terug zijn. Dit kan lang duren, zeker wanneer u veel ORFs tegelijk probeert
te BLASTen. Dit komt door technische beperkingen die in de huidige versie nog voorkomen,
onze excuses hiervoor.
Wanneer de BLAST klaar is zullen de resultaten in de tabel worden weergeven voor het
geselecteerde open reading frame. Als u resultaten van een ander open reading frame
wilt zien kunt u deze selecteren in het dropdown menu. Een lege tabel betekend dat
er geen significante BLAST resultaten zijn gevonden.
De tabel is zo opgebouwd dat u resultaten kunt sorteren door op een van de kolommen
te klikken. Nog een keer klikken draait het sorteren om (hoog > laag naar laag > hoog).
Tevens kunt u kolommen verslepen naar een andere locatie naar wens.


DATABASE:
Naast het ophalen uit de database kunt u ook resultaten verwijderen en opslaan.

Om resultaten op te slaan dient u bij 'Data opties' 'Sla resultaat op' aangeklikt te
hebben en op 'Voer uit in database' te klikken. Dit werkt alleen als u minstens een
header en sequentie ingeladen hebt.

Het verwijderen van resultaten kan handig zijn, aangezien iedere header slechts een
keer voor mag komen in de database. Om een resultaat te verwijderen hoeft u enkel
de exacte header in te voeren naast 'Header', 'Verwijder resultaat' te selecteren
en op 'Voer uit in database' te klikken.


LOKAAL:
Naast het lokaal ophalen van resultaten kunt u ze uiteraard ook opslaan en
verwijderen.

Om resultaten op te slaan dient u bij 'Data opties' 'Sla resultaat op' aangeklikt te
hebben en op 'Voer lokaal uit' te klikken. Dit werkt alleen als u minstens een header,
sequentie en open reading frame hebt ingeladen.

Het verwijderen van resultaten kan handig zijn, aangezien iedere header slechts een
keer opgeslagen kan worden. Om een resultaat te verwijderen hoeft u enkel de exacte
header in te voeren naast 'Header', 'Verwijder resultaat' te selecteren en op
'Voer lokaal uit' te klikken.


AFWIJKING ONTWERP:
Voor het lokaal verwerken van resultaten is een aparte module geschreven. Deze keuze
is gemaakt voor een betere opdeling van functionaliteiten in aparte modules, omdat
het werken met de database heel anders is van het verwerken van lokale resultaten.
