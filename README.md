# chess-engine

<h2>BugHouse do šachových enginov</h2>

<h4>Patrik Černý, školiteľ: Mrg. Šimon Sádovský.</h4>

<h5>Cieľ zimného semestra: </h5> Implementovať bughouse do existujúceho šachového enginu a začať so zlepšovaním jeho časovej zložitosti pomocou techník používaných pri klasických enginoch, napr AplhaBeta cutoffs, MoveOrdering atď.. 


<h5>Report mojej práce z prvého semestra:</h5>

Prácu som začal dôkladnou analýzou chess enginov TSCP a Sunsetter a analýzou ich kódu som postupne zistoval aké algoritmy a štruktúry používajú na implementáciu enginu. Následne som sa neúspešne pokúsil do spomenutých enginov doprogramovať bughouse mód pre šach, avšak kvôli nedostatku skúseností s c++/c sa mi to ani po dlhej práci nepodarilo. Preto som sa rozhodol pre vlastnú implementáciu aj šachu ako takého aj enginu, ktorý budem používať. Použil som Javu, v ktorej som pracoval počas štúdia najviac. Ako základný engine algoritmus som si vybral minimax, ktorý mám v pláne nahradiť Alpha-beta algoritmom v ďaľšom semestri. Ako programovaciu techniku som využil Immutables, ktoré sa principiálne hodia na programovanie šachu a enginu. Bughouse engine je funkčný, okrem zopár bugov ktoré treba vyriešiť, vo svojom súkromnom commite mám uložený aj engine pre šach klasický ako aj funkčné GUI pre klasický šach, ktoré v ďalšom semestri rozšírim aj pre bughouse. Ako som spomínal, samotná hra využíva MiniMax, ktorý je upravený pre potreby bughouse šachu, pravidlá šachu sú samozrejme upravené tiež. Minimax využíva pomocnú funkciu Evaluate, ktorá ohodnotí pozíciu v ktorej sa aktuálne nachádzame podľa viacerých pravidiel, a vracia enginu najlepší nájdený ťah v jeho zadanej hĺbke. Engine zatiaľ zvláda hĺbku 1 až 5 v rozumnom čase(do 1 min). Samozrejme hĺbka priamo ovplyvňuje kvalitu šachovej hry enginu, ale je vidieť že engine vyberá lepšie ťahy a neprehráva hru rozdávaním figúr. 

<h5> Kam bude projekt pokračovať ? </h5>
Ako som spomínal v reporte, Minimax bude nahradený Alpha-betou, pridanie dependency enginu na čase(aktuálne engine prehľadáva strom pozícií až kým neprejde všetky pozície v uživateľom zadanej hĺbke), v pláne je pridanie techník na zlepšenie časovej zložitosti a dorobenie GUI pre šachový mód bughouse ako aj klasický šach. 
