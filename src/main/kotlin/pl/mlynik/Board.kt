package pl.mlynik

private object Core {
    fun range(): IntRange {
        return 0..7
    }
}

enum class Player {
    White, Black;
}

val Player.opponent: Player
    get() {
        return if (this == Player.Black) Player.White else Player.Black
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
    var player = Player.White
        get() = field
        private set(value) {
            field = value
        }
    var checked: Player? = null
        get() = field
        private set(value) {
            field = value
        }

    private var fields = mutableMapOf<Field, Piece?>()

    init {
        for (y in Core.range())
            for (x in Core.range())
                fields[Field(x, y)] = null
    }

    sealed class AtResult {
        object OutOfBounds : AtResult()
        data class Occupied(val piece: Piece) : AtResult()
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
        object StillInCheck : MoveResult()

        object IllegalMove : Board.MoveResult()
        object OutOfTurn : MoveResult()
        object OutOfBounds : MoveResult()
        object EmptySpace : MoveResult()
    }

    fun move(fromField: Field, toField: Field): MoveResult {
        if (!fields.containsKey(toField)) return MoveResult.OutOfBounds
        if (!fields.containsKey(fromField)) return MoveResult.OutOfBounds

        val moving = fields[fromField] ?: return MoveResult.EmptySpace

        if (moving.player != player) {
            return MoveResult.OutOfTurn
        }

        val movesAllowed = moving.moves(fromField, this)
        val movedTo = fields[toField]
        if (!movesAllowed.contains(toField)) {
            return MoveResult.IllegalMove
        }

        fun validMove(checked: Player?) =
            if (movedTo != null && moving.player != movedTo.player) {
                MoveResult.ValidMove(movedTo, checked)
            } else {
                MoveResult.ValidMove(null, checked)
            }

        val draftFields = fields.toMutableMap()

        draftFields[fromField] = null
        draftFields[toField] = moving

        val stillCheck = stillCheck(draftFields)
        if (stillCheck) {
            return MoveResult.StillInCheck
        }

        checked = checked(player, moving, toField)

        val r = validMove(checked)

        player = player.opponent
        fields = draftFields

        return r
    }

    private fun stillCheck(draftFields: MutableMap<Field, Piece?>): Boolean {
        val playerKingPosition = draftFields
            .entries
            .firstOrNull { it.value == King(player) }
            ?.key
            ?: return false

        val www = draftFields
            .filter { it.value?.player == player.opponent }
            .mapNotNull { it.value?.moves(it.key, this) }

        return www.any { it.contains(playerKingPosition) }
    }

    private fun checked(attacking: Player, moving: Piece, toField: Field): Player? {
        val opponent = attacking.opponent
        val moves = moving.moves(toField, this).filter { at(it) == AtResult.Occupied(King(opponent)) }

        return if (moves.isNotEmpty()) {
            opponent
        } else {
            null
        }
    }

    fun copy(): Board {
        val board = Board()
        board.player = player
        board.checked = checked
        board.fields = fields.toMutableMap()
        return board
    }

    operator fun set(field: Field, value: Piece) {
        fields[field] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board) return false

        if (player != other.player) return false
        if (checked != other.checked) return false
        if (fields != other.fields) return false

        return true
    }

    override fun hashCode(): Int {
        return fields.hashCode()
    }

    companion object {
        fun empty(): Board {
            return Board()
        }
    }
}
