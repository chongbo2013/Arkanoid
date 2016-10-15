/*
 * This file is part of Jump Don't Die
 * Copyright (C) 2015 Dani Rodríguez <danirod@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.arkanoid.Screens;

import com.badlogic.gdx.Gdx;

/**
 * Some class for defining constant values used in the game, so that they can be changed from
 * a single location instead of lurking the code to find the values.
 */
public class CuasiConstantes {

    static void inicializar() {
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        PPM = SCREEN_WIDTH / SCREEN_WIDTH_METER;
        MPP = 1f / PPM;

        SCREEN_HEIGHT_METER = SCREEN_HEIGHT / PPM;

        TILE_SIZE = SCREEN_WIDTH_METER / 32f;
        BOARD_WIDTH = TILE_SIZE * 26f;
        BOARD_HEIGHT = TILE_SIZE * 30f;
        OFFSET_X = (SCREEN_WIDTH_METER - BOARD_WIDTH) / 2f;
        OFFSET_Y = (SCREEN_HEIGHT_METER - BOARD_HEIGHT) / 2f;
    }

    /**
     * Ancho de la pantalla en metros, esta es la unica constante respecto a las dimenciones, las demas dependen de esta
     */
    public static final float SCREEN_WIDTH_METER = 80f;

    /**
     * Height of screen
     */
    public static float SCREEN_HEIGHT_METER;

    /**
     * Board size
     */
    public static float TILE_SIZE = 2.5f, BOARD_WIDTH = 65f, BOARD_HEIGHT = 75f;

    /**
     * Maxima cantidad de nodos permitidas por BOX2D
     */
    public static final int MAX_NODES = 8;

    /**
     * Dimensiones de la pantalla
     */
    public static float SCREEN_WIDTH;
    public static float SCREEN_HEIGHT;

    /**
     * Puntos por metro
     */
    public static float PPM;

    /**
     * Metros por punto
     */
    public static float MPP;

    /**
     * Game offset left
     */
    public static float OFFSET_X;

    /**
     * Game offset bottom
     */
    public static float OFFSET_Y;

    /**
     * The force in N/s that the player uses to jump in an impulse. This force will also be applied
     * in the opposite direction to make the player fall faster multiplied by some value to make
     * it stronger.
     */
    public static final int IMPULSE_JUMP = 20;

}
