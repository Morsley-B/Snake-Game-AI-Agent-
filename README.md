Snake Game AI Agent (Java)

An autonomous agent developed for the **Wits Snake Game** — a real-time competitive multi-agent environment where snakes must navigate, collect apples, avoid collisions, and survive against zombies, obstacles, and other player-controlled snakes.

About This Project
This agent was built by **extending a provided skeleton codebase** and developing custom logic for real-time pathfinding, decision-making under uncertainty, and safe navigation in a dynamic grid.

The environment is complex, requiring the agent to weigh risks from zombies, walls, and other snakes, while prioritizing apple collection and survival.

Objective
Control a snake to:
-  Navigate a 50x50 grid
-  Reach apples before other agents
-  Avoid zombies and other snakes
-  Make real-time decisions in adversarial situations

Features
-  **Breadth-First Search (BFS)** for shortest-path navigation to apples
-  **Dynamic collision and danger zone detection**
-  **Fallback movement strategy** when no direct path is safe
-  **Head-to-head conflict resolution** (avoids competing with other snakes at close proximity)
-  **Zombie proximity logic** to avoid deadly zones
-  **Dead-end avoidance using safety heuristics**
-  **Structured logging** for decision transparency and debugging

 
 Algorithmic Highlights
### Pathfinding
- **BFS** is used to compute the shortest safe path to the apple.
- If the BFS path is unsafe, the agent switches to a **directional fallback strategy**.

### Safety Evaluation
- Each move is validated based on:
  - Grid boundaries
  - Obstacles and walls
  - Other snakes’ heads (proximity check to prevent suicide moves)
  - Zombie zones

### Path Reconstruction
- When BFS finds a valid path, a parent-pointer map is used to reconstruct the shortest path step-by-step.

How to Run
Make sure you have the **Wits Snake Game framework** installed and set up.
