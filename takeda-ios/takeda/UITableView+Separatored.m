//
//  UITableView+Separatored.m
//  iMedicum
//
//  Created by Alexander Rudenko on 08.11.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "UITableView+Separatored.h"

@implementation UITableView (Separatored)

-(UIImageView*)separ{
    UIImageView *s = [[UIImageView alloc] initWithFrame:CGRectMake(0, -0.5, self.width, 0.5)];
    s.backgroundColor = self.separatorColor;
    return s;
}

-(UIImageView*)topSepar{
    UIImageView *s = [[UIImageView alloc] initWithFrame:CGRectMake(0, self.tableHeaderView.height+0.5, self.width, 0.5)];
    s.backgroundColor = self.separatorColor;
    return s;
}

@end
