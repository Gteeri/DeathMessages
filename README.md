# DeathMessages

Custom death messages plugin for Paper 1.21+. Replaces vanilla death messages with configurable, randomized messages per death cause.

## Features

- Custom messages per death cause (PvP, fall, drowning, fire, explosion, void, mob)
- Random message selection from lists
- Color code support with `&` formatting
- Coordinate display toggle
- Multi-language support (EN/RU)
- In-game reload and toggle

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/deathmsg reload` | Reload all configs | `deathmessages.admin` |
| `/deathmsg toggle` | Enable/disable plugin | `deathmessages.admin` |

Alias: `/dm`

## Placeholders

| Placeholder | Description |
|-------------|-------------|
| `{victim}` | Player who died |
| `{killer}` | Player who killed (PvP only) |
| `{weapon}` | Weapon used (PvP only) |
| `{mob}` | Mob that killed |
| `{world}` | World name |
| `{x}` | X coordinate |
| `{y}` | Y coordinate |
| `{z}` | Z coordinate |

## Configuration

### config.yml

```yaml
enabled: true
use-random: true
show-coordinates: false
language: en
```

### messages.yml

```yaml
player-kill:
  - "&c{victim} &7was slain by &c{killer} &7using &f{weapon}"
fall:
  - "&c{victim} &7fell from a high place"
  - "&c{victim} &7didn't bounce"
```

## Installation

1. Download the JAR file
2. Place it in your server's `plugins/` folder
3. Restart the server
4. Edit configs in `plugins/DeathMessages/`
5. Use `/deathmsg reload` to apply changes

## Requirements

- Paper 1.21+
- Java 21
