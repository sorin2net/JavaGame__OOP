# JavaGameOOP

## RED HOOD - 2D Dark Fantasy RPG

<img src="imgs/skelly_enemy.png" width="700">


A fully object-oriented Java game that showcases the power of OOP principles in game design. Built with a focus on clean architecture, modular structure, and maintainable code, this project combines solid programming fundamentals with fun and challenging gameplay in a dark-fantasy setting.

### 📖 Story

Red Hood, a brave and skilled wizard, discovers an ancient manuscript that reveals the existence of a legendary magic potion - a brew capable of granting mastery over any spell. However, Valthros, an evil wizard with plans for world domination, is seeking the same ingredients.

The ingredients are hidden in a cursed cemetery, guarded by the spirits of those who tried and failed to create the potion. Red Hood must face waves of enemies, deadly traps, and the darkness itself to stop Valthros and save the world.

### ✨ Features

**Gameplay**
* **5 Progressive Levels** - Each level introduces new mechanics and increases in difficulty
* **Dual Combat System** - Melee and ranged attacks with stamina management
* **Ingredient Collection** - Find magic potions to progress and win
* **NPC Interaction** - Friendly wizards offer tips and stories
* **Fog of War System** - Limited visibility in dark levels
* **Save/Load System** - Save progress and continue later

**Enemies and Challenge**
* **4 Unique Enemy Types** - Each with its own mechanics and distinct AI
* **Epic Boss Fight** - Final confrontation with Valthros, the evil wizard
* **Progressive Design** - Every mechanic learned is essential to finish the game
* **Strategic Positioning** - Enemies are placed to create tactical challenges

**Audio and Atmosphere**
* **Original Soundtrack** - 3 musical tracks for menu and levels
* **Sound Effects** - 9 sound effects for actions and interactions
* **Full Audio Control** - Adjust music and effects volume independently

**OOP Architecture**
* **Modular Structure** - Clean, organized, and easy-to-maintain code
* **OOP Principles** - Encapsulation, inheritance, polymorphism, and abstraction
* **Design Patterns** - Factory Method, Abstract Factory, State Pattern
* **SQL Database** - Data persistence and player progress



### ✨ Features

**Gameplay**
* **5 Progressive Levels** - Each level introduces new mechanics and increases in difficulty
* **Dual Combat System** - Melee and ranged attacks with stamina management
* **Ingredient Collection** - Find magic potions to progress and win
* **NPC Interaction** - Friendly wizards offer tips and stories
* **Fog of War System** - Limited visibility in dark levels
* **Save/Load System** - Save progress and continue later

**Enemies and Challenge**
* **4 Unique Enemy Types** - Each with its own mechanics and distinct AI
* **Epic Boss Fight** - Final confrontation with Valthros, the evil wizard
* **Progressive Design** - Every mechanic learned is essential to finish the game
* **Strategic Positioning** - Enemies are placed to create tactical challenges

**Audio and Atmosphere**
* **Original Soundtrack** - 3 musical tracks for menu and levels
* **Sound Effects** - 9 sound effects for actions and interactions
* **Full Audio Control** - Adjust music and effects volume independently

**OOP Architecture**
* **Modular Structure** - Clean, organized, and easy-to-maintain code
* **OOP Principles** - Encapsulation, inheritance, polymorphism, and abstraction
* **Design Patterns** - Factory Method, Abstract Factory, State Pattern
* **SQL Database** - Data persistence and player progress

---

### 🎮 Controls

| Action | Key |
| :--- | :---: |
| Move Left/Right | `A` / `D` |
| Jump | `W` / `SPACE` |
| Melee Attack | `Left Click` |
| Ranged Attack | `Q` |
| Interact with NPC | `E` |
| Pause | `ESC` |

### 🖼️ Screenshots

**Main Menu**

<img src="imgs/main_menu.png" width="700">


*Intuitive interface with options for Play, Settings, Scores, and Quit*




### 🎮 Gameplay Core

**Dynamic Camera**
<br>
Smoothly follows the character across the entire map, offering a cinematic experience.
<br><br>
<p align="center">
  <img src="imgs/npc_dialogue.png" width="700" alt="Dynamic Camera & NPC">
</p>

<br>

**Advanced Platforming & Destructible Objects**
<br>
Explore on ground, bridges, clouds, and many other surfaces. Discover potions by breaking strategically placed crates and barrels.
<br><br>
<p align="center">
  <img src="imgs/platforming_and_barrels.png" width="700" alt="Platforming">
</p>

<br>

**Fog of War**
<br>
Dark levels limit player visibility, creating tension and requiring careful exploration.
<br><br>
<p align="center">
  <img src="imgs/enemy_golems.png" width="700" alt="Fog of War">
</p>

<br>

**Trap System**
<br>
Avoid lethal spikes, automated cannons, and toxic tar zones that require quick reflexes and planning.

<table width="100%">
  <tr>
    <td width="50%" align="center">
      <img src="imgs/trap.png" alt="Spikes" width="95%">
      <br><i>Lethal Spikes</i>
    </td>
    <td width="50%" align="center">
      <img src="imgs/cannon.png" alt="Cannons" width="95%">
      <br><i>Automated Cannons</i>
    </td>
  </tr>
</table>

---

### ⚔️ Enemy Types

<table width="100%">
  <tr>
    <td width="50%">
      <b>Sword Skeletons</b><br>
      The first enemies encountered, guarding the cemetery entrance. They offer a gentle introduction to the game's combat system.
    </td>
    <td width="50%" align="center">
      <img src="imgs/skelly_enemy.png" width="95%" alt="Sword Skeleton">
    </td>
  </tr>
  <tr>
    <td width="50%">
      <b>Mace Skeletons</b><br>
      Stronger and more aggressive variants that can occasionally spawn poison potions. Defeating them requires more advanced tactics.
    </td>
    <td width="50%" align="center">
      <img src="imgs/tough_skelly.png" width="95%" alt="Mace Skeleton">
    </td>
  </tr>
  <tr>
    <td width="50%">
      <b>Golems</b><br>
      Massive crypt guardians that increase in speed and aggression when wounded. A major danger requiring careful resource management.
    </td>
    <td width="50%" align="center">
      <img src="imgs/golem_attacking.png" width="95%" alt="Golem">
    </td>
  </tr>
</table>

---

### 💀 Boss Fight

**Valthros (Final Boss)**
<br>
The evil wizard with devastating attacks, both melee and ranged. Permanent health bar on screen. The final confrontation requires all skills learned throughout the game.

<table width="100%">
  <tr>
    <td width="50%" align="center">
      <img src="imgs/boss.png" alt="Valthros Boss" width="95%">
      <br><i>The Final Confrontation</i>
    </td>
    <td width="50%" align="center">
      <img src="imgs/boss_dash.png" alt="Boss Dash Attack" width="95%">
      <br><i>Devastating Dash Attack</i>
    </td>
  </tr>
</table>

---

### ⚙️ Interfaces and Systems

**Settings**
<p align="center">
  <img src="imgs/settings.png" width="700" alt="Settings Menu">
</p>

**Game States**
<table width="100%">
  <tr>
    <td width="50%" align="center">
      <img src="imgs/level_completed_screen.png" alt="Level Completed" width="95%">
      <br><i>Victory Screen</i>
    </td>
    <td width="50%" align="center">
      <img src="imgs/you_died_screen.png" alt="Game Over" width="95%">
      <br><i>Game Over Screen</i>
    </td>
  </tr>
</table>
