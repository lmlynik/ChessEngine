package pl.mlynik

fun Board.Companion.withFields(vararg fields: Field): WithFieldsBuilder {
    return WithFieldsBuilder(fields)
}

class WithFieldsBuilder(
    private val fields: Array<out Field>,
    private var board: Board = Board.empty()
) {

    fun filledWith(piece: () -> Piece): WithFieldsBuilder {
        fields.forEach { board[it] = piece() }
        return this
    }

    fun build(): Board {
        return board
    }

    fun withFields(vararg fields: Field): WithFieldsBuilder {
        return WithFieldsBuilder(
            fields,
            board
        )
    }
}
