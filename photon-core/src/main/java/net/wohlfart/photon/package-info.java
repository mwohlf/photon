package net.wohlfart.photon;

/*

  working on:

  todos:
  - athmosphere shader
  - scaling inside solar system according to:
    http://www.gamasutra.com/view/feature/131393/a_realtime_procedural_universe_.php?print=1
    - using double for position in IEntity
    - add only entities to the scene graph, entities contain units that are added to the render tree,
      entities are added to the semantic tree
    - fix shader with scale parameters
    - updating entities has to propagate changes to the commands/scene graph

  - figure out the windows problems
  - setup as applet or webstart
  - refactor label to single char VAO, reuse use any shader for the drawing code
  - add particles for moving effect
  - atmosphere shader
  - action framework
  - blender loader
  - models
  - hud
  - network code
  - user input
  - actually do pick elements
  - solar flare shader
  - lightsource shader
  - moving to JOGL
  - blender model loader
  - glow shader
  - refactor and simplify picking ray (use only one event)
  - basic particle system
  - dust particles
  - simplify hud code/ layout code
  - implement removing entities/renderCommands

  done:
  - shader loading with string template
  - organize renderElem and renderCmd


 */

