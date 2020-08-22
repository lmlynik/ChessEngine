package pl.mlynik

abstract class Piece(val player: Player, val symbol: Char) {

    abstract fun moves(field: Field, board: Board): Set<Field>

    override fun toString(): String {
        return "${this.player}${this.javaClass.simpleName}"
    }

    fun occupiedByFriend(at: Board.AtResult): Boolean {
        return if (at is Board.AtResult.Occupied) {
            at.piece.player == player
        } else {
            false
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Piece) return false

        if (player != other.player) return false
        if (symbol != other.symbol) return false

        return true
    }

    override fun hashCode(): Int {
        var result = player.hashCode()
        result = 31 * result + symbol.hashCode()
        return result
    }
}

class Pawn(player: Player) : Piece(player, 'p') {

    override fun moves(field: Field, board: Board): Set<Field> {
        val list = mutableSetOf<Field>()
        val upwards = field + when (player) {
            Player.Black -> Rank.Down
            else -> Rank.Up
        }

        if (board.at(upwards) == Board.AtResult.Empty) {
            list.add(upwards)
        }

        fun directed(direction: Direction) {
            val r = board.at(upwards + direction)
            if (r is Board.AtResult.Occupied && !occupiedByFriend(r)) {
                list.add(upwards + direction)
            }
        }

        directed(Direction.Right)
        directed(Direction.Left)
        return list
    }
}

class Rook(player: Player) : Piece(player, 'r') {

    override fun moves(field: Field, board: Board): Set<Field> {
        return setOf()
    }
}

class Bishop(player: Player) : Piece(player, 'b') {

    override fun moves(field: Field, board: Board): Set<Field> {
        return setOf()
    }
}

class Knight(player: Player) : Piece(player, 'n') {

    override fun moves(field: Field, board: Board): Set<Field> {
        return listOf(
            field + Rank.Up + Rank.Up + Direction.Left,
            field + Rank.Up + Rank.Up + Direction.Right,

            field + Rank.Down + Rank.Down + Direction.Left,
            field + Rank.Down + Rank.Down + Direction.Right,

            field + Direction.Left + Direction.Left + Rank.Up,
            field + Direction.Left + Direction.Left + Rank.Down,

            field + Direction.Right + Direction.Right + Rank.Up,
            field + Direction.Right + Direction.Right + Rank.Down
        )
            .filter { it.isValid() }
            .filterNot {
                occupiedByFriend(board.at(it))
            }
            .toSet()
    }
}

class King(player: Player) : Piece(player, 'k') {

    override fun moves(field: Field, board: Board): Set<Field> {
        return listOf(
            field + Rank.Up,
            field + Rank.Down,
            field + Direction.Left,
            field + Direction.Right,
            field + Rank.Up + Direction.Left,
            field + Rank.Up + Direction.Right,
            field + Rank.Down + Direction.Left,
            field + Rank.Down + Direction.Right
        )
            .filter { it.isValid() }
            .filterNot {
                occupiedByFriend(board.at(it))
            }
            .toSet()
    }
}

class Queen(player: Player) : Piece(player, 'q') {

    override fun moves(field: Field, board: Board): Set<Field> {
        return setOf()
    }
}
