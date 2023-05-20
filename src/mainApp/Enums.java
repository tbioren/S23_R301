package mainApp;

/**
 * This is the FitnessMethod enum. It selects the fitness method
 */
enum FitnessMethod {
    ONES, COMPARE_TO_CHROMOSOME, SWITCHES, DECREASING_SIGNIFICANCE, TROUGHS
}

/**
 * This is the SelectionMethod enum. It selects the selection method
 */
enum SelectionMethod {
    TOP_HALF, ROULETTE, RANK, BEST_RANDOM_WORST, TRUNCATION
}