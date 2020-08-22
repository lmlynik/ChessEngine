package pl.mlynik

private object Core {
    fun range(): IntRange {
        return 0..7
    }
}

enum class Player {
    White, Black
}

data class Field(val x: Int, val y: Int) {

    fun isValid(): Boolean {
        return x in Core.range() && y in Core.range()
    }

    operator fun plus(rank: Rank): Field {
        return when (rank) {
            Rank.Up -> Field(x, y + 1)
            Rank.Down -> Field(x, y - 1)
        }
    }

    operator fun plus(direction: Direction): Field {
        return when (direction) {
            Direction.Left -> Field(x - 1, y)
            Direction.Right -> Field(x + 1, y)
        }
    }
}

enum class Rank {
    Up, Down
}

enum class Direction {
    Left, Right
}

class Board {
    private var player = Player.White
        get() = field
    private val fields = mutableMapOf<Field, Piece?>()

    init {
        for (y in Core.range())
            for (x in Core.range())
                fields[Field(x, y)] = null
    }

    sealed class AtResult {
        object OutOfBounds : AtResult()
        class Occupied(val piece: Piece) : AtResult()
        object Empty : AtResult()
    }

    fun at(field: Field): AtResult {
        if (!fields.containsKey(field)) return AtResult.OutOfBounds

        val piece = fields[field]
        return if (piece == null)
            AtResult.Empty
        else
            AtResult.Occupied(piece)
    }

    sealed class MoveResult {
        class ValidMove(val attacked: Piece?, val checked: Player?) : MoveResult()
        object IllegalMove : Board.MoveResult()

        object OutOfBounds : MoveResult()
        object EmptySpace : MoveResult()
    }

    fun move(fromField: Field, toField: Field): MoveResult {
        if (!fields.containsKey(toField)) return MoveResult.OutOfBounds
        if (!fields.containsKey(fromField)) return MoveResult.OutOfBounds

        val moving = fields[fromField] ?: return MoveResult.EmptySpace
        val movesAllowed = moving.moves(fromField, this)
        val movedTo = fields[toField]
        if (!movesAllowed.contains(toField)) {
            return MoveResult.IllegalMove
        }

        val result =
            if (movedTo != null && moving.player != movedTo.player) {
                MoveResult.ValidMove(movedTo, checked())
            } else {
                MoveResult.ValidMove(null, checked())
            }

        player = if (player == Player.White) Player.Black else Player.White
        fields[fromField] = null
        fields[toField] = moving
        return result
    }

    private fun checked(): Player? {
        return null
    }

    operator fun set(field: Field, value: Piece) {
        fields[field] = value
    }

    companion object {
        fun empty(): Board {
            return Board()
        }
    }
}
