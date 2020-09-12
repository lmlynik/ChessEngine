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

    sealed class AtResult {
        data class Occupied(val piece: Piece) : AtResult()
        object Empty : AtResult()
    }

    fun at(field: Field): AtResult {
        fields[field] ?: return AtResult.Empty

        val piece = fields[field]
        return if (piece == null)
            AtResult.Empty
        else
            AtResult.Occupied(piece)
    }

    sealed class MoveResult {
        class ValidMove(val attacked: Piece?, val checked: Player?) : MoveResult()
        class IllegalMove(val piece: Piece) : Board.MoveResult()
        class OutOfTurn(val playerInTurn: Player) : MoveResult()
        class OutOfBounds(val field: Field) : MoveResult()
        object StillInCheck : MoveResult()

        object EmptySpace : MoveResult()
    }

    fun move(fromField: Field, toField: Field): MoveResult {
        if (!toField.isValid()) return MoveResult.OutOfBounds(toField)
        if (!fromField.isValid()) return MoveResult.OutOfBounds(fromField)

        val moving = fields[fromField] ?: return MoveResult.EmptySpace

        if (moving.player != player) {
            return MoveResult.OutOfTurn(player)
        }

        val movesAllowed = moving.moves(fromField, this)
        val movedTo = fields[toField]
        if (!movesAllowed.contains(toField)) {
            return MoveResult.IllegalMove(moving)
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

        if (checked == null) {
            checked = if (testChecked(player.opponent, draftFields)) player.opponent else null
        } else {
            if (testChecked(player, draftFields)) {
                return MoveResult.StillInCheck
            }
        }

        val r = validMove(checked)

        player = player.opponent
        fields = draftFields

        return r
    }

    private fun testChecked(player: Player, fields: MutableMap<Field, Piece?>): Boolean {
        val playerKingPosition = fields
            .entries
            .firstOrNull { it.value == King(player) }
            ?.key
            ?: return false

        val www = fields
            .filter { it.value?.player == player.opponent }
            .mapNotNull { it.value?.moves(it.key, this.copy(fields)) }

        return www.any { it.contains(playerKingPosition) }
    }

    fun copy(appliedFields: MutableMap<Field, Piece?> = fields.toMutableMap()): Board {
        val board = Board()
        board.player = player
        board.checked = checked
        board.fields = appliedFields
        return board
    }

    fun with(field: Field, value: Piece): Board {
        val board = copy()
        board.fields[field] = value
        return board
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
        @JvmStatic
        fun empty(): Board {
            return Board()
        }

        fun with(field: Field, value: Piece): Board {
            val board = empty()
            board.fields[field] = value
            return board
        }
    }
}
