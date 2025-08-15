package com.example.notes.navigation


//@Composable
//fun Navigation() {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = Router.NotesRouter.route
//    ) {
//        composable(route = Router.NotesRouter.route) {
//            AllNoteScreen(
//
//                navigateToEditNote = {
//
//                    navController.navigate(
//
//                        Router.NoteRouter(it.toString()).route
//
//                    )
//
//                },
//
//                navigateToNewNote = {
//                    navController.navigate(
//                        Router.NoteRouter.newNoteRoute
//
//                    )
//                }
//
//            )
//        }
//        composable(
//            route = Router.NoteRouter.route,
//            arguments = listOf(
//                navArgument(
//                    name = Router.NoteRouter.TOKEN,
//                ) {
//                    type = NavType.IntType
////                    defaultValue = -1
//                },
//            )
//        ) {
//            SingleNoteScreen(navigateBack = {
//                navController.navigateUp()
//            })
//        }
//    }
//
//}