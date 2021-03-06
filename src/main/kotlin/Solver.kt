package solve

import akka.actor.ActorSystem
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActor

/**
 * A group of "sovlers" that solve games!
 *
 * @param Pos the data type that represents a game position (or state).
 * @param Move the data type that represents a game move.
 */
class Solver<Pos, Move> : UntypedActor() {

    var actors: MutableList<ActorRef> = arrayListOf()
    
    /**
     * Given a hashed number, get the appropriate actor.
     * @param hash the hashed integer.
     * @return the actor associated with the particular hash.
     */
    fun actorForHash(hash: Int): ActorRef {
        return actors.get(hash % Config.ACTOR_COUNT)
    }
    
    /**
     * Run the solver as a whole!
     * 
     * Responsible for initializing the actor system and then starting it off!
     *
     * @param game the game to be solved.
     */
    fun run(game: Game<Pos, Move>) {
        val system = ActorSystem.create("SolverSystem")
        for (i in 1..Config.ACTOR_COUNT) {
            actors.add(
                system.actorOf(Props.create(Solver::class.java), "Solver" + i.toString())
            )
        }
        val master = actorForHash(game.hashPosition(game.initialPos))
    }

    val solvedPositions: MutableMap<Pos, Primitive> =  mutableMapOf()

     /**
      * Determine if a particular position has been solved. If not, return null.
      *
      * @param position the position to be looked up.
      * @return the primitive of the value of the position if it exists.
      */
     fun lookup(position: Pos): Primitive? {
        if (solvedPositions.containsKey(position)) {
            return solvedPositions[position]
        } else {
            return null
        }
     }

     /**
      * Given a particular position, resolve the position if it can be resolved.
      * If it can't update the counter to indicate that it only needs so many more
      * iterations until it can be.
      *
      * @param position to be resolved.
      * @return success (or failure) of resolving the position.
      */
     fun resolve(position: Pos): Boolean {
         // TODO.
         return false
     }
    
     /**
      * Generate a positions children and distribute them to everyone.
      *
      * @param Pos he position to be distributed.
      */
     fun distribute(position: Pos) {
     }

    override fun onReceive(msg: Any?) {
        when (msg) {
            is Lookup<*> -> {
                val result = lookup(msg.position as Pos)
                if (result != null) {
                    // TODO: Send back to appropriate actor.
                } else {
                    distribute(msg.position as Pos)
                }
            }
            is Resolve<*> ->  {
                if (resolve(msg.position as Pos)) {
                    // The position was successfully resolved.
                    // TODO: Send back to appropriate actor.
                } else {
                    // Still needs to wait for stuff to finish.
                    // TODO?
                }
            }
        }
    }

 }
