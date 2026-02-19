# Java Netzwerk-Sniffer

Minimaler Sniffer mit einer einzelnen `main`-Methode und Endlosschleife, die Pakete ausgibt.

⚠️ Achtung: Die korrekte Funktionsweise ist von mehreren Faktoren abhängig (z. B. installierte pcap-Bibliothek, Betriebssystem, Berechtigungen). 
D.h. es erfordert ggf. etwas Fehlersuche, um es zum Laufen zu bringen. Aber das ist ja auch Teil des Lernprozesses! :)

## Voraussetzungen

- Java 25 (!)
- Maven
- Native pcap-Bibliothek (plattformabhaengig, siehe unten).

## Installation der pcap-Bibliothek

### macOS

- `libpcap` ist in der Regel bereits vorhanden.
- Falls gewuenscht via Homebrew:

```bash
brew install libpcap
```

### Linux

Debian/Ubuntu:

```bash
sudo apt-get update
sudo apt-get install -y libpcap-dev
```

Fedora/RHEL:

```bash
sudo dnf install -y libpcap-devel
```

Arch:

```bash
sudo pacman -S libpcap
```

### Windows

- Installiere **Npcap** im WinPcap-kompatiblen Modus: https://npcap.com/
- Danach ist die pcap-API systemweit verfuegbar.

## Ausfuehren

Build in Intellij: Maven-Toolfenster -> Lifecycle -> `package` (erstmals) oder `compile` (bei Aenderungen).

Danach die `main`-Methode der Klasse `Sniffer` starten.


## Hinweise

- Sniffing erfordert in der Regel Admin-/Root-Rechte.
  - Linux: z. B. mit `sudo` starten oder `cap_net_raw,cap_net_admin` setzen.
  - macOS/Windows: als Administrator ausfuehren.
- Bei `UnsatisfiedLinkError` fehlt die native jnetpcap-Bibliothek im `java.library.path`.
