package pl.mlynik

abstract class Piece(val player: Player, val symbol: Char) {

    abstract fun moves(field: Field, board: Board): Set<Field>

    override fun toString(): String {
        return "${this.player}${this.javaClass.simpleName}"
    }

//    fun occupiedBy(at: Board.AtResult, p: Player = player): Boolean {
//        return if (at is Board.AtResult.Occupied) {
//            at.piece.player == p
//        } else {
//            false
//        }
//    }

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
            if (r.occupiedBy(player.opponent)) {
                list.add(upwards + direction)
            }
        }

        directed(Direction.Right)
        directed(Direction.Left)
        return list
    }
}

private fun Board.AtResult.occupiedBy(player: Player): Boolean {
    return if (this is Board.AtResult.Occupied) {
        this.piece.player == player
    } else {
        false
    }
}

class Rook(player: Player) : Piece(player, 'r') {

    override fun moves(field: Field, board: Board): Set<Field> {
        fun scan(f: (field: Field) -> Field): List<Field> {
            fun scanAcc(fieldD: Field, acc: List<Field>): List<Field> {
                val df = f(fieldD)
                val at = board.at(df)
                return if (!df.isValid()) {
                    acc
                } else if (at.occupiedBy(player)) {
                    acc
                } else if (at.occupiedBy(player.opponent)) {
                    acc + df
                } else {
                    scanAcc(df, acc + df)
                }
            }

            return scanAcc(field, listOf())
        }

        return (
                scan { it + Rank.Up }
                        + scan { it + Rank.Down }
                        + scan { it + Direction.Left }
                        + scan { it + Direction.Right }
                ).toSet()
    }
}

class Bishop(player: Player) : Piece(player, 'b') {

    override fun moves(field: Field, board: Board): Set<Field> {
        return setOf(

        )
    }
}

class Knight(player: Player) : Piece(player, 'n') {

    override fun moves(field: Field, board: Board): Set<Field> {
        return setOf(
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
                board.at(it).occupiedBy(player)
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
                board.at(it).occupiedBy(player)
            }
            .toSet()
    }
}

class Queen(player: Player) : Piece(player, 'q') {

    override fun moves(field: Field, board: Board): Set<Field> {
        return setOf()
    }
}
